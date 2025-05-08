package com.acroninspector.app.presentation.activity.main

import android.Manifest
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.KEY_IS_AFTER_LOGOUT
import com.acroninspector.app.common.constants.Constants.LOAD_DATA_FROM_SERVER
import com.acroninspector.app.common.constants.Constants.NAV
import com.acroninspector.app.common.constants.Constants.UPLOAD_DATA_TO_SERVER
import com.acroninspector.app.common.constants.PreferencesConstants
import com.acroninspector.app.common.extension.toHexString
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.common.utils.FlashlightManager
import com.acroninspector.app.databinding.ActivityMainBinding
import com.acroninspector.app.databinding.CounterMenuBinding
import com.acroninspector.app.databinding.MenuHeaderBinding
import com.acroninspector.app.di.activity.main.MainModule
import com.acroninspector.app.domain.entity.local.database.User
import com.acroninspector.app.domain.entity.remote.request.values.DataLog
import com.acroninspector.app.domain.entity.remote.request.values.DataLogStorage
import com.acroninspector.app.presentation.custom.listener.IOnBackPressed
import com.acroninspector.app.presentation.custom.listener.NetworkStatusHandler
import com.acroninspector.app.presentation.custom.listener.NfcStatusHandler
import com.acroninspector.app.presentation.custom.listener.NotificationsCounterHandler
import com.acroninspector.app.presentation.custom.listener.ScanNfcListener
import com.acroninspector.app.presentation.dialog.BrightnessDialog
import com.acroninspector.app.presentation.dialog.ErrorDialog
import com.acroninspector.app.presentation.dialog.ProgressDialog
import com.acroninspector.app.presentation.dialog.SuccessDialog
import com.acroninspector.app.presentation.dialog.UserCardDialog
import com.acroninspector.app.presentation.dialog.UserLoginDialog
import com.acroninspector.app.presentation.fragment.comment.listener.PassCommentListener
import com.acroninspector.app.presentation.fragment.defectparameters.listener.PassDefectCauseListener
import com.acroninspector.app.presentation.fragment.defectparameters.listener.PassDefectNameListener
import com.acroninspector.app.presentation.mvp.BaseActivity
import com.acroninspector.app.support.ConnectionProvider
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.Lazy
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.navigation_view
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.nfcManager
import java.io.File
import javax.inject.Inject


class MainActivity : BaseActivity(), MainView, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var headerBinding: MenuHeaderBinding

    private lateinit var navController: NavController

    private lateinit var flashlightManager: FlashlightManager

    private lateinit var connectionDisposable: Disposable

    private lateinit var prefs: SharedPreferences

    private var networkStatusHandlers: MutableList<NetworkStatusHandler?> = ArrayList()

    private var nfcStatusHandlers: MutableList<NfcStatusHandler?> = ArrayList()

    private var notificationsCounterHandlers: MutableList<NotificationsCounterHandler?> =
        ArrayList()

    private var passDefectCauseListener: PassDefectCauseListener? = null

    private var passDefectNameListener: PassDefectNameListener? = null

    private var passCommentListener: PassCommentListener? = null

    private var notificationsCount = 0

    private var isNetworkEnabled = false

    private var shouldCloseDrawer = true

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var connectionProvider: ConnectionProvider

    @Inject
    lateinit var userCardDialog: Lazy<UserCardDialog>

    @Inject
    lateinit var userLoginDialog: Lazy<UserLoginDialog>

    @Inject
    lateinit var brightnessDialog: Lazy<BrightnessDialog>


    @Inject
    lateinit var techList: Array<Array<String>>

    @ProvidePresenter
    fun providePresenter(): MainPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.getApp(applicationContext).componentsHolder.getComponent(
            javaClass,
            MainModule(applicationContext)
        ).inject(this)

        loadThemePreference()

        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)

        binding = getDataBindingView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        headerBinding = DataBindingUtil.bind(binding.navigationView.getHeaderView(0))!!
        headerBinding.handler = this

        flashlightManager = FlashlightManager(this)

        observeConnectionChanges()
    }

    override fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
    }

//    private fun writeToFile(data: DataLog, fileName: String) {
//        val file = File(getExternalFilesDir(null), fileName)
//        val gson = Gson()
//        val dataJson = gson.toJson(data)
//
//        try {
//            FileOutputStream(file).use { output ->
//                output.write(dataJson.toByteArray())
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

    private fun writeToFile(data: DataLog, fileName: String): Boolean {
        return try {
            val fileUri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.Files.FileColumns._ID)
            val selection = "${MediaStore.Files.FileColumns.DISPLAY_NAME} = ?"
            val selectionArgs = arrayOf(fileName)

            val cursor = contentResolver.query(fileUri, projection, selection, selectionArgs, null)
            cursor?.use {
                if (it.count > 0) {
                    return false
                }
            }

            val values = ContentValues().apply {
                put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
                put(MediaStore.Files.FileColumns.MIME_TYPE, "text/plain")
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }

            val uri = contentResolver.insert(fileUri, values) ?: return false

            contentResolver.openOutputStream(uri)?.use { output ->
                val gson = Gson()
                val dataJson = gson.toJson(data)
                output.write(dataJson.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun readFilesFromStorage() {
        val directory =
            File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOCUMENTS)
        val filesInDirectory = directory.listFiles()

        filesInDirectory?.forEach { file ->
            if (!DataLogStorage.file.contains(file)) {
                DataLogStorage.file.add(file)
            }
        }
    }

    override fun dataLog() {
        val dataLog = DataLogStorage.getDataLog()
        val dateCreation =
            DateUtil.reversedConvertLongDateToString(Calendar.getInstance().timeInMillis)
        val fileName = "$dateCreation.txt"

        writeToFile(dataLog, fileName)
        readFilesFromStorage()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun saveNetworkStatus(isNetworkEnabled: Boolean) {
        val sharedPreferences =
            getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(Constants.NET, isNetworkEnabled)
        editor.apply()
    }

    private fun getNetworkStatus(): Boolean {
        val sharedPreferences =
            getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constants.NET, false)
    }

    private fun saveNavigationId(id: Int) {
        val sharedPreferences =
            getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(NAV, id)
        editor.apply()
    }

    private fun getNavigationId(): Int {
        val sharedPreferences =
            getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)
        return sharedPreferences.getInt(NAV, 0)
    }

    override fun onStart() {
        super.onStart()
//        ---СОХРАНЯЕТСЯ ID ФРАГМЕНТ НЕ ТОЛЬКО КОТОРЫЙ В НАВИГАЦИИ НАДО ДЕЛАТЬ НАВЕРНОЕ ПРОВЕРКУ---
        val navigationId = getNavigationId()
        if (navigationId == 0) {
            val currentDestinationId = navController.currentDestination?.id
            if (currentDestinationId != null) {
                navController.navigate(currentDestinationId)
            }
        } else {
            try {
                navController.navigate(navigationId)
            } catch (e: IllegalArgumentException) {
                Log.e("err", "$e")
            }
        }

        observeConnectionChanges()
        refreshNetworkStatus(getNetworkStatus())
    }

    override fun onStop() {
        super.onStop()
        connectionDisposable.dispose()
    }

    private fun observeConnectionChanges() {
        connectionDisposable =
            connectionProvider.observeConnectionChanges().subscribe { isNetworkEnabled ->
                saveNetworkStatus(isNetworkEnabled)
                refreshNetworkStatus(isNetworkEnabled)
                presenter.refreshSessionId()
            }
    }

    override fun onResume() {
        super.onResume()

        pendingIntentForDefiningNfc()
    }

    private fun pendingIntentForDefiningNfc() {
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val filter = IntentFilter().apply {
            addAction(NfcAdapter.ACTION_TAG_DISCOVERED)
            addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            addAction(NfcAdapter.ACTION_TECH_DISCOVERED)
        }
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, arrayOf(filter), techList)
    }


    override fun setMenuResource(menuResourceId: Int, checkedItemId: Int) {
        binding.navigationView.inflateMenu(menuResourceId)
        binding.navigationView.setCheckedItem(checkedItemId)
    }

    override fun setupNavController(navigationGraphId: Int) {
        navController.setGraph(navigationGraphId)

        NavigationUI.setupWithNavController(binding.navigationView, navController)
        binding.navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        shouldCloseDrawer = true

        when (item.itemId) {
            R.id.notificationsViewPagerFragment -> {
                navController.navigate(R.id.action_global_notificationsViewPagerFragment)
            }

            R.id.tasksViewPagerFragment -> {
                navController.navigate(R.id.action_global_tasksViewPagerFragment)
            }

            R.id.rootEquipmentFragment -> {
                navController.navigate(R.id.action_global_rootEquipmentFragment)
            }

            R.id.defectsFragment -> {
                navController.navigate(R.id.action_global_defectsFragment)
            }

            R.id.drawer_item_get_data -> {
                presenter.syncDataWithServer(LOAD_DATA_FROM_SERVER)
            }

            R.id.drawer_item_send_data -> {
                presenter.syncDataWithServer(UPLOAD_DATA_TO_SERVER)

                this.lifecycleScope.launch(Dispatchers.IO) {
                    delay(300)
                    val cacheDir = this@MainActivity.cacheDir
                    cacheDir.deleteRecursively()
                    val codeCacheDir = this@MainActivity.codeCacheDir
                    codeCacheDir.deleteRecursively()
                    val cacheDir2 = this@MainActivity.externalCacheDir
                    cacheDir2?.deleteRecursively()
                }
            }

            R.id.drawer_item_exit -> {
                presenter.logout()
            }

            R.id.drawer_item_flashlight -> {
                flashlightManager.toggleFlashlight()
                shouldCloseDrawer = false
            }

            R.id.drawer_item_brightness -> {
                showBrightnessDialog()
            }

            R.id.switch_light_dark -> {
                val currentNightMode = AppCompatDelegate.getDefaultNightMode()
                val newMode = when (currentNightMode) {
                    AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_NO
                }
                saveThemeMode(newMode)
                updateTheme(newMode)
            }
        }

        if (shouldCloseDrawer) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        saveNavigationId(item.itemId)
        return true
    }

    private fun updateTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    override fun recreate() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent)
        finishAfterTransition()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun loadThemePreference() {
        val sharedPreferences =
            getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)
        val savedThemeMode =
            sharedPreferences.getInt(Constants.THEME_MODE, AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(savedThemeMode)
    }

    private fun saveThemeMode(mode: Int) {
        val sharedPreferences =
            getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(Constants.THEME_MODE, mode)
        editor.apply()
    }

    override fun clearPicFolder() {
        application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let { storageDir ->
            if (storageDir.exists() && storageDir.isDirectory) {
                val files = storageDir.listFiles()
                if (files != null) {
                    for (file in files) {
                        file.delete()
                    }
                }
            }
        }
    }

    override fun clearSharedTheme() {
        val sharedPreferences =
            getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(Constants.THEME_MODE)
        editor.remove(NAV)
        editor.apply()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val fragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            val currentFragment =
                fragment?.childFragmentManager?.fragments?.get(0) as? ScanNfcListener

            currentFragment?.onNfcScanned(
                intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)!!.toHexString()
            )
        }
    }

    override fun setNotificationsCount(count: Int) {
        val menuItem = binding.navigationView.menu.findItem(R.id.notificationsViewPagerFragment)
        if (menuItem != null) {
            val actionView = menuItem.actionView
            if (actionView != null) {
                val counterMenuBinding: CounterMenuBinding = DataBindingUtil.bind(actionView)!!
                counterMenuBinding.count = count
                notificationsCount = count

                for (handler in notificationsCounterHandlers) {
                    handler?.handleNotificationsCount(count)
                }
            }
        }
    }


    override fun setUser(user: User) {
        headerBinding.user = user
    }

    override fun setFunctionName(functionName: Int) {
        headerBinding.functionName = getString(functionName)
    }

    override fun setAppVersionName(appVersion: String) {
        headerBinding.tvAppVersion.text = getString(R.string.app_version, appVersion)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_NFC_SETTINGS) refreshNfcStatus()
    }

    fun setPassDefectCauseListener(listener: PassDefectCauseListener) {
        passDefectCauseListener = listener
    }

    fun setPassDefectNameListener(listener: PassDefectNameListener) {
        passDefectNameListener = listener
    }

    fun setPassDefectCommentListener(listener: PassCommentListener) {
        passCommentListener = listener
    }

    fun passDefectCauseId(defectCauseId: Int) {
        passDefectCauseListener?.passDefectCauseId(defectCauseId)
    }

    fun passDefectNameId(defectNameId: Int) {
        passDefectNameListener?.passDefectNameId(defectNameId)
    }

    fun passDefectComment(comment: String) {
        passCommentListener?.passComment(comment)
    }

    fun setNetworkStatusHandler(networkStatusHandler: NetworkStatusHandler) {
        networkStatusHandlers.add(networkStatusHandler)
        refreshNetworkStatus(isNetworkEnabled)
    }

    fun setNfcStatusHandler(nfcStatusHandler: NfcStatusHandler) {
        nfcStatusHandlers.add(nfcStatusHandler)
        refreshNfcStatus()
    }

    fun setNotificationsCounterHandler(notificationsCounterHandler: NotificationsCounterHandler) {
        notificationsCounterHandlers.add(notificationsCounterHandler)
        notificationsCounterHandler.handleNotificationsCount(notificationsCount)
    }

    fun releaseDefectCauseListener() {
        passDefectCauseListener = null
    }

    fun releaseDefectNameListener() {
        passDefectNameListener = null
    }

    fun releaseDefectCommentListener() {
        passCommentListener = null
    }

    fun releaseNetworkStatusHandler(networkStatusHandler: NetworkStatusHandler) {
        networkStatusHandlers.remove(networkStatusHandler)
    }

    fun releaseNfcStatusHandler(nfcStatusHandler: NfcStatusHandler) {
        nfcStatusHandlers.remove(nfcStatusHandler)
    }

    fun releaseNotificationsCounterHandler(notificationsCounterHandler: NotificationsCounterHandler) {
        notificationsCounterHandlers.remove(notificationsCounterHandler)
    }

    override fun refreshNetworkStatus(isNetworkEnabled: Boolean) {
        this.isNetworkEnabled = isNetworkEnabled

        headerBinding.isNetworkEnabled = isNetworkEnabled

        for (handler in networkStatusHandlers) handler?.handleNetworkStatus(isNetworkEnabled)

    }

    override fun refreshNfcStatus() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            headerBinding.isNfcEnabled = nfcManager.defaultAdapter.isEnabled

            for (handler in nfcStatusHandlers) handler?.handleNfcStatus(nfcManager.defaultAdapter.isEnabled)
        }
    }

    override fun showSnackbar(resourceId: Int) = showSnackbar(getString(resourceId))

    override fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackbar.view.backgroundColor = ContextCompat.getColor(this, R.color.colorRed)
        val textView =
            snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER

        snackbar.show()
    }

    fun onClickUserName() = presenter.onUserNameClicked()

    override fun openNfcSettingsActivity() {
        startActivityForResult(
            Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS),
            Constants.REQUEST_CODE_NFC_SETTINGS
        )
    }

    override fun openLoginActivity() {
        saveThemeMode(AppCompatDelegate.MODE_NIGHT_NO)

        val args = Bundle()
        args.putBoolean(KEY_IS_AFTER_LOGOUT, true)

        Handler().postDelayed({
            navController.navigate(R.id.loginActivity, args)
            finish()
        }, 250)
    }


    override fun lockDrawer() = binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

    override fun unlockDrawer() = binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)

    override fun openDrawer() = binding.drawerLayout.openDrawer(GravityCompat.START)

    override fun closeDrawer() = binding.drawerLayout.closeDrawer(GravityCompat.START)

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val currentFragment = fragment?.childFragmentManager?.fragments?.get(0) as? IOnBackPressed

        if (currentFragment != null) {
            currentFragment.onBackPressed().takeIf { !it }?.let {
                closeFragmentIfDrawerNotOpen()
            }
        } else closeFragmentIfDrawerNotOpen()
    }

    private fun closeFragmentIfDrawerNotOpen() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else super.onBackPressed()
    }

    override fun showUserCardDialog() {
        val dialog = supportFragmentManager.findFragmentByTag(UserCardDialog.TAG)
        if (dialog == null) {
            userCardDialog.get().show(supportFragmentManager, UserCardDialog.TAG)
        }
    }

    override fun showUserLoginDialog() {
        val dialog = supportFragmentManager.findFragmentByTag(UserLoginDialog.TAG)
        if (dialog == null) {
            userLoginDialog.get().show(supportFragmentManager, UserLoginDialog.TAG)
        }
    }

    private fun showBrightnessDialog() {
        val dialog = supportFragmentManager.findFragmentByTag(BrightnessDialog.TAG)
        if (dialog == null) {
            brightnessDialog.get().show(supportFragmentManager, BrightnessDialog.TAG)
        }
    }


    override fun setProgressTextToSyncingDataDialog(
        @StringRes entityNameResourceId: Int, currentNumber: Int, entitiesCount: Int
    ) {
        val dialog = supportFragmentManager.findFragmentByTag(ProgressDialog.TAG) as ProgressDialog
        dialog.setProgressText(entityNameResourceId, currentNumber, entitiesCount)
    }

    /**
     * @param eventType is loading or uploading data
     */
    override fun showProgressDialog(eventType: Int) {
        val dialog = supportFragmentManager.findFragmentByTag(ProgressDialog.TAG)
        if (dialog == null) {
            ProgressDialog(eventType).show(supportFragmentManager, ProgressDialog.TAG)
            supportFragmentManager.executePendingTransactions()
        }
    }

    /**
     * @see showProgressDialog comment
     */
    override fun showSyncSuccessDialog(eventType: Int) {
        val dialog = supportFragmentManager.findFragmentByTag(SuccessDialog.TAG)
        if (dialog == null) {
            SuccessDialog(eventType).show(supportFragmentManager, SuccessDialog.TAG)
            supportFragmentManager.executePendingTransactions()
        }
    }

    override fun showSyncErrorDialog(
        @StringRes messageResourceId: Int, @StringRes entityResourceId: Int
    ) {
        val dialog = supportFragmentManager.findFragmentByTag(ErrorDialog.TAG)
        if (dialog == null) {
            val message = if (entityResourceId != 0) {
                val entity = getString(entityResourceId)
                getString(messageResourceId, entity)
            } else getString(messageResourceId)

            ErrorDialog(getString(R.string.error), message).show(
                supportFragmentManager, ErrorDialog.TAG
            )
            supportFragmentManager.executePendingTransactions()
        }
    }

    override fun closeProgressDialog() {
        val dialog = supportFragmentManager.findFragmentByTag(ProgressDialog.TAG)
        if (dialog != null && dialog is ProgressDialog) {
            dialog.dismiss()
            supportFragmentManager.executePendingTransactions()
        }
    }

    override fun onPause() {
        super.onPause()

        //---СОХРАНЯЕТСЯ ID ФРАГМЕНТ НЕ ТОЛЬКО, КОТОРЫЙ В НАВИГАЦИИ, НАДО ДЕЛАТЬ НАВЕРНОЕ ПРОВЕРКУ---
//         saveNavigationId(navController.currentDestination?.id ?: 0)

        val menu = navigation_view.menu

        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val itemId = item.itemId

            if (itemId != navController.currentDestination?.id) {
                Log.d(
                    "tests",
                    "Пункт меню не соответствует активному фрагменту ((${navController.currentDestination?.id}))"
                )
                navController.currentDestination?.id = binding.navigationView.checkedItem!!.itemId
                Log.d(
                    "tests",
                    "обновленный navController.currentDestination = ((${navController.currentDestination?.id}))"
                )
                saveNavigationId(navController.currentDestination?.id ?: 0)
                break
            }
        }


        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onDestroy() {
        connectionDisposable.dispose()
        isNetworkEnabled = false
        super.onDestroy()
        if (isFinishing) {
            App.getApp(this).componentsHolder.releaseComponent(javaClass)
        }
        val sharedPreferences =
            getSharedPreferences(PreferencesConstants.APP_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(NAV)
        editor.apply()
    }

}