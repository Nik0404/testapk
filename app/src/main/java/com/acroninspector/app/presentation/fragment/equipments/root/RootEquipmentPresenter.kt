package com.acroninspector.app.presentation.fragment.equipments.root

import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.presentation.fragment.equipments.EquipmentBasePresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class RootEquipmentPresenter(
        equipmentInteractor: EquipmentInteractor,
        scanNfcInteractor: ScanNfcInteractor
) : EquipmentBasePresenter<RootEquipmentView>(equipmentInteractor, scanNfcInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadEquipments(-1)
    }
}
