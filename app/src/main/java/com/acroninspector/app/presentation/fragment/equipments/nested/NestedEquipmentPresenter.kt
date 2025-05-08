package com.acroninspector.app.presentation.fragment.equipments.nested

import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.presentation.fragment.equipments.EquipmentBasePresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class NestedEquipmentPresenter(
        equipmentInteractor: EquipmentInteractor,
        scanNfcInteractor: ScanNfcInteractor
) : EquipmentBasePresenter<NestedEquipmentView>(equipmentInteractor, scanNfcInteractor) {

    var searchQuery = ""

    fun onDirectoryInfoPrepared(directoryId: Int, directoryName: String) {
        if (searchQuery.isEmpty()) {
            viewState.setToolbarTitle(directoryName)
            loadEquipments(directoryId)
        } else {
            viewState.setToolbarTitle(searchQuery)
            searchEquipments(searchQuery)
        }
    }
}
