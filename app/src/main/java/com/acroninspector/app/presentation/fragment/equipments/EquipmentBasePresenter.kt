package com.acroninspector.app.presentation.fragment.equipments

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.domain.entity.local.display.DisplayDirectory
import com.acroninspector.app.domain.entity.local.display.DisplayEquipment
import com.acroninspector.app.domain.entity.local.display.DisplayEquipmentItem
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class EquipmentBasePresenter<ViewType : EquipmentBaseView>(
        private val equipmentInteractor: EquipmentInteractor,
        private val scanNfcInteractor: ScanNfcInteractor
) : BasePresenter<ViewType>() {

    private var displayEquipments = listOf<DisplayEquipment>()

    private var loadEquipmentsDisposable: Disposable? = null

    private var searchEquipmentsDisposable: Disposable? = null

    private val setEquipmentsToAdapterConsumer: ((it: List<DisplayEquipment>) -> Unit) = {
        viewState.hideProgress()
        viewState.updateEquipments(it)

        handleEmptyState(it.isEmpty())
        displayEquipments = it
    }

    private val showErrorConsumer: ((it: Throwable) -> Unit) = {
        viewState.hideProgress()
        viewState.showSnackbar(R.string.error_message)

        Timber.e(it)
    }

    protected fun loadEquipments(parentId: Int) {
        disposeLoadEquipmentsDisposable()

        viewState.showProgress()
        loadEquipmentsDisposable = equipmentInteractor
                .getCombinedEquipmentsAndDirectories(parentId)
                .subscribe(setEquipmentsToAdapterConsumer, showErrorConsumer)
    }

    protected fun searchEquipments(searchQuery: String) {
        disposeSearchEquipmentsDisposable()

        viewState.showProgress()
        searchEquipmentsDisposable = equipmentInteractor
                .getSearchedEquipmentsAndDirectories(searchQuery)
                .subscribe(setEquipmentsToAdapterConsumer, showErrorConsumer)
    }

    fun onDirectoryClicked(position: Int) {
        val directory = displayEquipments[position] as DisplayDirectory
        viewState.showEquipmentItemsByDirectory(directory.id, directory.name)
    }

    fun onEquipmentClicked(position: Int) {
        val functionId = equipmentInteractor.getFunctionId()

        val equipmentId = (displayEquipments[position] as DisplayEquipmentItem).id
        if (functionId == Functions.REGISTERING_LABELS) {
            viewState.showEquipmentNfcDialog(equipmentId)
        } else {
            viewState.showEquipmentByPassDialog(equipmentId)
        }
    }

    fun onDefectListClicked(equipmentId: Int) {
        viewState.openDefectListFragment(equipmentId)
    }

    fun onRegisterDefectClicked(equipmentId: Int) {
        viewState.openRegisterDefectFragment(equipmentId)
    }

    fun onAddNfcMarkClicked(equipmentId: Int, keyEquipmentScreen: Int) {
        viewState.openDefineNfcFragment(equipmentId, keyEquipmentScreen)
    }

    fun onNfcScanned(nfcCode: String) {
        viewState.showProgress()
        subscriptions.add(scanNfcInteractor
                .getEquipmentIdByNfcCode(nfcCode)
                .subscribe({
                    viewState.hideProgress()

                    val functionId = equipmentInteractor.getFunctionId()
                    if (functionId == Functions.REGISTERING_LABELS) {
                        viewState.showEquipmentNfcDialog(it)
                    } else viewState.showEquipmentByPassDialog(it)
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }, {
                    viewState.hideProgress()
                    viewState.showNfcErrorDialog(nfcCode)
                }))
    }

    private fun handleEmptyState(listIsEmpty: Boolean) {
        if (listIsEmpty) {
            viewState.showEmptyState()
        } else viewState.hideEmptyState()
    }

    private fun disposeSearchEquipmentsDisposable() {
        if (searchEquipmentsDisposable != null && !searchEquipmentsDisposable!!.isDisposed)
            searchEquipmentsDisposable!!.dispose()
    }

    private fun disposeLoadEquipmentsDisposable() {
        if (loadEquipmentsDisposable != null && !loadEquipmentsDisposable!!.isDisposed)
            loadEquipmentsDisposable!!.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeSearchEquipmentsDisposable()
        disposeLoadEquipmentsDisposable()
    }
}
