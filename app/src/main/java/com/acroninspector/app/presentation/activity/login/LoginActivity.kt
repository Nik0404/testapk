package com.acroninspector.app.presentation.activity.login

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.NfcF
import android.nfc.tech.NfcV
import android.os.Bundle
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.extension.toDecString
import com.acroninspector.app.presentation.custom.listener.NfcStatusHandler
import com.acroninspector.app.presentation.custom.listener.ScanNfcListener
import com.acroninspector.app.presentation.fragment.login.auth.LoginFragment
import com.acroninspector.app.presentation.mvp.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import org.jetbrains.anko.nfcManager
import kotlin.math.abs


class LoginActivity : BaseActivity() {

    private var techList: Array<Array<String>> = arrayOf(
        arrayOf(
            NfcA::class.java.name,
            NfcB::class.java.name,
            NfcF::class.java.name,
            NfcV::class.java.name,
            IsoDep::class.java.name,
            MifareClassic::class.java.name,
            MifareUltralight::class.java.name,
            Ndef::class.java.name
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadBackground()

        if (savedInstanceState == null) {
            val isAfterLogout =
                intent.getBooleanExtra(Constants.KEY_IS_AFTER_LOGOUT, false)

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, LoginFragment.newInstance(isAfterLogout))
                .commit()
        }
    }

    private fun loadBackground() {
        val drawable = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            R.drawable.activity_login_background_low_api
        } else R.drawable.activity_login_background

        val backgroundBitmap = BitmapFactory.decodeResource(resources, drawable)
        val croppedBitmap = cropBitmap(backgroundBitmap)
        setBitmapToWindow(croppedBitmap)
    }

    private fun cropBitmap(sourceBitmap: Bitmap): Bitmap {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)

        val screenWidth = size.x.toDouble()
        val screenHeight = size.y.toDouble()

        val imageWidth = sourceBitmap.width.toDouble()
        val imageHeight = sourceBitmap.height.toDouble()

        val heightsRelation = (screenHeight / imageHeight)
        val widthsRelation = (screenWidth / imageWidth)

        //I don't know why mutliplying image width to magicNumber works correctly, but it works.
        val magicNumber = 0.9
        return when {
            heightsRelation > widthsRelation -> {
                val newImageWidth = screenWidth / heightsRelation
                Bitmap.createBitmap(
                    sourceBitmap,
                    (abs((imageWidth - newImageWidth)) / 2).toInt(),
                    0,
                    (newImageWidth * magicNumber).toInt(),
                    imageHeight.toInt()
                )
            }

            heightsRelation < widthsRelation -> {
                val newImageHeight = screenHeight / widthsRelation
                Bitmap.createBitmap(
                    sourceBitmap,
                    0,
                    (abs((imageHeight - newImageHeight)) / 2).toInt(),
                    (imageWidth * magicNumber).toInt(),
                    newImageHeight.toInt()
                )
            }

            else -> sourceBitmap
        }
    }

    private fun setBitmapToWindow(bitmap: Bitmap) {
        Glide.with(this)
            .asBitmap().load(bitmap)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) = Unit
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    window?.setBackgroundDrawable(BitmapDrawable(resources, resource))
                }
            })
    }


    /**
     * Блок работы с NFC
     */
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val byteId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)!!
            val userCardId = byteId.toDecString()
            val fragment = supportFragmentManager
                .fragments
                .first { it is LoginFragment }
                as? ScanNfcListener
            fragment?.onNfcScanned(userCardId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_NFC_SETTINGS)
            refreshNfcStatus()
    }

    private var nfcStatusHandlers: MutableList<NfcStatusHandler?> = ArrayList()

    private fun refreshNfcStatus() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            for (handler in nfcStatusHandlers)
                handler?.handleNfcStatus(nfcManager.defaultAdapter.isEnabled)
        }
    }

    override fun onPause() {
        super.onPause()

        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter?.disableForegroundDispatch(this)
    }
}
