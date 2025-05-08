package com.acroninspector.app.presentation.fragment.equipments.nested

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.ENTITY_EQUIPMENT
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_ID
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_TYPE
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_ID
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_SCREEN
import com.acroninspector.app.common.constants.Constants.KEY_IS_FROM_SEARCH
import com.acroninspector.app.common.constants.Constants.KEY_SEARCH_ENTITY
import com.acroninspector.app.common.constants.Constants.KEY_SEARCH_QUERY
import com.acroninspector.app.common.constants.Constants.NESTED_EQUIPMENT_SCREEN
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogByPass
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogNfc
import com.acroninspector.app.presentation.fragment.equipments.EquipmentBaseFragment
import com.acroninspector.app.presentation.fragment.equipments.EquipmentBasePresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_nested_equipment.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class NestedEquipmentFragment : EquipmentBaseFragment(), NestedEquipmentView {

    @Inject
    lateinit var nfcEquipmentDialog: Provider<EquipmentCardDialogNfc>

    @Inject
    lateinit var byPassEquipmentDialog: Provider<EquipmentCardDialogByPass>

    @Inject
    @InjectPresenter
    lateinit var presenter: NestedEquipmentPresenter

    @ProvidePresenter
    fun providePresenter(): NestedEquipmentPresenter = presenter

    @ProvidePresenterTag(presenterClass = NestedEquipmentPresenter::class)
    fun providePresenterTag(): String {
        val directoryId = getDirectoryIdFromArguments()
        val directoryName = getDirectoryNameFromArguments()
        return directoryId.toString() + directoryName
    }

    private val viewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.backButton -> Navigation.findNavController(view!!).popBackStack()
        }
    }

    private fun getDirectoryIdFromArguments(): Int {
        return arguments!!.getInt(KEY_DIRECTORY_ID)
    }

    private fun getDirectoryNameFromArguments(): String {
        return arguments!!.getString(KEY_DIRECTORY_NAME, "")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null && arguments != null) {
            presenter.searchQuery = arguments?.getString(KEY_SEARCH_QUERY, "")!!

            val directoryId = getDirectoryIdFromArguments()
            val directoryName = getDirectoryNameFromArguments()

            presenter.onDirectoryInfoPrepared(directoryId, directoryName)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainView)?.lockDrawer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nested_equipment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        equipmentsAdapter.setClickListener(this)
        backButton.setOnClickListener(viewClickListener)

        val isFromSearch = arguments?.getBoolean(KEY_IS_FROM_SEARCH, false)!!
        if (isFromSearch) {
            nestedEquipmentsFabSearch.hide()
        }
    }

    override fun openSearchFragment() {
        (activity as? MainView)?.lockDrawer()

        val args = Bundle()
        args.putInt(KEY_SEARCH_ENTITY, ENTITY_EQUIPMENT)

        Navigation.findNavController(view!!)
                .navigate(R.id.action_nestedEquipmentFragment_to_searchFragment, args)
    }

    override fun openDefineNfcFragment(equipmentId: Int, equipmentScreen: Int) {
        val args = Bundle()
        args.putInt(KEY_EQUIPMENT_ID, equipmentId)
        args.putInt(KEY_EQUIPMENT_SCREEN, equipmentScreen)

        try {
            Navigation.findNavController(view!!)
                    .navigate(R.id.action_nestedEquipmentFragment_to_defineNfcFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun openDefectListFragment(equipmentId: Int) {
        val args = Bundle()
        args.putInt(KEY_ENTITY_ID, equipmentId)
        args.putInt(KEY_ENTITY_TYPE, ENTITY_EQUIPMENT)

        try {
            Navigation.findNavController(view!!)
                    .navigate(R.id.action_nestedEquipmentFragment_to_defectsSearchResultFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun openRegisterDefectFragment(equipmentId: Int) {
        val args = Bundle()
        args.putInt(KEY_EQUIPMENT_ID, equipmentId)

        try {
            Navigation.findNavController(view!!)
                    .navigate(R.id.action_nestedEquipmentFragment_to_registerDefectFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun showEquipmentItemsByDirectory(directoryId: Int, directoryName: String) {
        val isFromSearch = arguments?.getBoolean(KEY_IS_FROM_SEARCH, false)!!

        val args = Bundle(1).apply {
            putInt(KEY_DIRECTORY_ID, directoryId)
            putString(KEY_DIRECTORY_NAME, directoryName)
            putBoolean(KEY_IS_FROM_SEARCH, isFromSearch)
        }

        try {
            Navigation.findNavController(view!!)
                    .navigate(R.id.action_nestedEquipmentFragment_to_nestedEquipmentFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun showEquipmentNfcDialog(equipmentId: Int) {
        val dialog = childFragmentManager.findFragmentByTag(EquipmentCardDialogNfc.TAG)
        if (dialog == null) {
            nfcEquipmentDialog.get().show(childFragmentManager, EquipmentCardDialogNfc.TAG,
                    onClickAddNfc = { presenter.onAddNfcMarkClicked(equipmentId, NESTED_EQUIPMENT_SCREEN) },
                    equipmentId = equipmentId)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun showEquipmentByPassDialog(equipmentId: Int) {
        val dialog = childFragmentManager.findFragmentByTag(EquipmentCardDialogByPass.TAG)
        if (dialog == null) {
            byPassEquipmentDialog.get().show(childFragmentManager, EquipmentCardDialogByPass.TAG,
                    onClickDefectList = { presenter.onDefectListClicked(equipmentId) },
                    onClickRegisterDefect = { presenter.onRegisterDefectClicked(equipmentId) },
                    equipmentId = equipmentId)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun setToolbarTitle(directoryName: String) {
        toolbarTitleView.text = directoryName
    }

    override fun showSnackbar(resourceId: Int) {
        getSnackbar(view!!, resourceId).show()
    }

    override fun getProgressBar(): View = progressBar

    override fun getAppBarUnderlineView(): View = appBarUnderlineView

    override fun getRecyclerView(): RecyclerView = equipmentItemsList

    override fun getEmptyStateView(): View = emptyStateView

    override fun getAccountButton(): View = accountButton

    override fun getNfcButton(): AppCompatImageButton = nfcButton

    override fun getNetworkIndicatorView(): AppCompatImageView = networkIndicatorView

    override fun getFabSearch(): FloatingActionButton = nestedEquipmentsFabSearch

    override fun getBasePresenter(): EquipmentBasePresenter<NestedEquipmentView> = presenter

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }

    companion object {
        const val KEY_DIRECTORY_ID = "Key.Acron.DirectoryId"
        const val KEY_DIRECTORY_NAME = "Key.Acron.DirectoryName"
    }
}
