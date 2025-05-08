package com.acroninspector.app.presentation.fragment.equipments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.EQUIPMENT_DIVIDER
import com.acroninspector.app.common.constants.Constants.EQUIPMENT_FOLDER
import com.acroninspector.app.common.constants.Constants.EQUIPMENT_ITEM
import com.acroninspector.app.common.constants.Constants.EQUIPMENT_SPACE
import com.acroninspector.app.domain.entity.local.display.DisplayEquipment
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.equipments.EquipmentsAdapter
import com.acroninspector.app.presentation.custom.CustomDataBindingAttributes
import com.acroninspector.app.presentation.custom.listener.NetworkStatusHandler
import com.acroninspector.app.presentation.custom.listener.NfcStatusHandler
import com.acroninspector.app.presentation.custom.listener.NotificationsCounterHandler
import com.acroninspector.app.presentation.custom.listener.ScanNfcListener
import com.acroninspector.app.presentation.dialog.ErrorDialog
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import timber.log.Timber

abstract class EquipmentBaseFragment : BaseFragment(), EquipmentBaseView, NetworkStatusHandler,
        NfcStatusHandler, NotificationsCounterHandler, EquipmentsAdapter.OnClickEquipmentListener,
        ScanNfcListener {

    protected lateinit var equipmentsAdapter: EquipmentsAdapter

    private val viewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.accountButton -> (activity as? MainView)?.showUserCardDialog()
            R.id.nfcButton -> (activity as? MainView)?.openNfcSettingsActivity()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val equipmentType = equipmentsAdapter.equipmentList[position].getEquipmentType()
                return when (equipmentType) {
                    EQUIPMENT_ITEM -> 2
                    EQUIPMENT_DIVIDER -> 2
                    EQUIPMENT_SPACE -> 2
                    EQUIPMENT_FOLDER -> 1
                    else -> throw IllegalArgumentException("Unknown equipmentType: $equipmentType")
                }
            }
        }

        getRecyclerView().addAcronScrollListenerToRecycler(layoutManager) { visibility ->
            try {
                getAppBarUnderlineView().visibility = visibility
            } catch (e: IllegalStateException) {
                Timber.e(e)
            }
        }

        equipmentsAdapter = EquipmentsAdapter()
        getRecyclerView().adapter = equipmentsAdapter
        getRecyclerView().layoutManager = layoutManager

        getAccountButton().setOnClickListener(viewClickListener)
        getNfcButton().setOnClickListener(viewClickListener)

        getFabSearch().onClick { openSearchFragment() }
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.setNfcStatusHandler(this)
        (activity as? MainActivity)?.setNetworkStatusHandler(this)
        (activity as? MainActivity)?.setNotificationsCounterHandler(this)
    }

    override fun onDirectoryClicked(position: Int) {
        getBasePresenter().onDirectoryClicked(position)
    }

    override fun onEquipmentClicked(position: Int) {
        getBasePresenter().onEquipmentClicked(position)
    }

    override fun onNfcScanned(nfcCode: String) {
        getBasePresenter().onNfcScanned(nfcCode)
    }

    override fun showNfcErrorDialog(nfcCode: String) {
        val dialog = childFragmentManager.findFragmentByTag(ErrorDialog.TAG)
        if (dialog == null) {
            ErrorDialog(getString(R.string.error), getString(R.string.scanned_nfc_tag_that_no_in_database, nfcCode))
                    .show(childFragmentManager, ErrorDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun handleNetworkStatus(isOnline: Boolean) {
        CustomDataBindingAttributes.setNetworkStatusImage(getNetworkIndicatorView(), isOnline)
    }

    override fun handleNfcStatus(isEnabled: Boolean) {
        CustomDataBindingAttributes.setNfcStatusColor(getNfcButton(), isEnabled)
    }

    override fun updateEquipments(equipments: List<DisplayEquipment>) {
        equipmentsAdapter.setData(equipments)
    }

    override fun showProgress() {
        getProgressBar().visibility = View.VISIBLE
    }

    override fun hideProgress() {
        getProgressBar().visibility = View.INVISIBLE
    }

    override fun showEmptyState() {
        getEmptyStateView().visibility = View.VISIBLE
    }

    override fun hideEmptyState() {
        getEmptyStateView().visibility = View.INVISIBLE
    }

    abstract fun getProgressBar(): View

    abstract fun getAppBarUnderlineView(): View

    abstract fun getRecyclerView(): RecyclerView

    abstract fun getEmptyStateView(): View

    abstract fun getAccountButton(): View

    abstract fun getNfcButton(): AppCompatImageButton

    abstract fun getNetworkIndicatorView(): AppCompatImageView

    abstract fun getFabSearch(): FloatingActionButton

    abstract fun getBasePresenter(): EquipmentBasePresenter<*>

    protected fun getSnackbar(view: View, resourceId: Int): Snackbar {
        val snackbar = Snackbar.make(view, getString(resourceId), Snackbar.LENGTH_SHORT)
        snackbar.view.backgroundColor = ContextCompat.getColor(context!!, R.color.colorRed)
        snackbar.setActionTextColor(Color.WHITE)

        return snackbar
    }

    override fun handleNotificationsCount(count: Int) = Unit

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.releaseNfcStatusHandler(this)
        (activity as? MainActivity)?.releaseNetworkStatusHandler(this)
        (activity as? MainActivity)?.releaseNotificationsCounterHandler(this)
    }
}
