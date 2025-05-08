package com.acroninspector.app.presentation.fragment.equipments.root

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.ENTITY_EQUIPMENT
import com.acroninspector.app.common.constants.Constants.KEY_SEARCH_ENTITY
import com.acroninspector.app.common.constants.Constants.ROOT_EQUIPMENT_SCREEN
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogByPass
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogNfc
import com.acroninspector.app.presentation.fragment.equipments.EquipmentBaseFragment
import com.acroninspector.app.presentation.fragment.equipments.EquipmentBasePresenter
import com.acroninspector.app.presentation.fragment.equipments.nested.NestedEquipmentFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_root_equipment.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class RootEquipmentFragment : EquipmentBaseFragment(), RootEquipmentView {

    @Inject
    lateinit var nfcEquipmentDialog: Provider<EquipmentCardDialogNfc>

    @Inject
    lateinit var byPassEquipmentDialog: Provider<EquipmentCardDialogByPass>

    @Inject
    @InjectPresenter
    lateinit var presenter: RootEquipmentPresenter

    @ProvidePresenter
    fun providePresenter(): RootEquipmentPresenter = presenter

    private val viewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.menuButton -> (activity as? MainView)?.openDrawer()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainView)?.unlockDrawer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_root_equipment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        equipmentsAdapter.setClickListener(this)
        menuButton.setOnClickListener(viewClickListener)
    }

    override fun openSearchFragment() {
        (activity as? MainView)?.lockDrawer()

        val args = Bundle()
        args.putInt(KEY_SEARCH_ENTITY, ENTITY_EQUIPMENT)

        Navigation.findNavController(view!!)
                .navigate(R.id.action_rootEquipmentFragment_to_searchFragment, args)
    }

    override fun openDefineNfcFragment(equipmentId: Int, equipmentScreen: Int) {
        val args = Bundle()
        args.putInt(Constants.KEY_EQUIPMENT_ID, equipmentId)
        args.putInt(Constants.KEY_EQUIPMENT_SCREEN, equipmentScreen)

        try {
            Navigation.findNavController(view!!)
                    .navigate(R.id.action_rootEquipmentFragment_to_defineNfcFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun openDefectListFragment(equipmentId: Int) {
        val args = Bundle()
        args.putInt(Constants.KEY_ENTITY_ID, equipmentId)
        args.putInt(Constants.KEY_ENTITY_TYPE, ENTITY_EQUIPMENT)

        try {
            Navigation.findNavController(view!!)
                    .navigate(R.id.action_rootEquipmentFragment_to_defectsSearchResultFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun openRegisterDefectFragment(equipmentId: Int) {
        val args = Bundle()
        args.putInt(Constants.KEY_EQUIPMENT_ID, equipmentId)

        try {
            Navigation.findNavController(view!!)
                    .navigate(R.id.action_rootEquipmentFragment_to_registerDefectFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun showEquipmentItemsByDirectory(directoryId: Int, directoryName: String) {
        val args = Bundle(2).apply {
            putInt(NestedEquipmentFragment.KEY_DIRECTORY_ID, directoryId)
            putString(NestedEquipmentFragment.KEY_DIRECTORY_NAME, directoryName)
        }

        try {
            Navigation.findNavController(view!!)
                    .navigate(R.id.action_rootEquipmentFragment_to_nestedEquipmentFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun showEquipmentNfcDialog(equipmentId: Int) {
        val dialog = childFragmentManager.findFragmentByTag(EquipmentCardDialogNfc.TAG)
        if (dialog == null) {
            nfcEquipmentDialog.get().show(childFragmentManager, EquipmentCardDialogNfc.TAG,
                    onClickAddNfc = { presenter.onAddNfcMarkClicked(equipmentId, ROOT_EQUIPMENT_SCREEN) },
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

    override fun getFabSearch(): FloatingActionButton = rootEquipmentsFabSearch

    override fun getBasePresenter(): EquipmentBasePresenter<RootEquipmentView> = presenter

    override fun handleNotificationsCount(count: Int) {
        if (count > 0) {
            notificationsCounterView.visibility = View.VISIBLE
            (notificationsCounterView as TextView).text = count.toString()
        } else {
            notificationsCounterView.visibility = View.INVISIBLE
        }
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}
