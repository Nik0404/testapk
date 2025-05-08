package com.acroninspector.app.presentation.fragment.nfc.nfcname

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import com.acroninspector.app.domain.interactors.nfc.NfcInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber

@InjectViewState
class NfcNamePresenter(
        private val interactor: NfcInteractor
) : BasePresenter<NfcNameView>() {

    var equipmentScreen: Int = Constants.DEFAULT_INVALID_SCREEN

    var equipmentId = Constants.DEFAULT_INVALID_ID

    var nfcEquipmentId = Constants.DEFAULT_INVALID_ID
    var nfcEquipmentCode = ""

    fun onSaveClicked(nfcName: String) {
        if (nfcName.length <= Constants.MAX_NFC_NAME_LENGTH) {
            if (nfcName.isNotEmpty() && nfcName.trim().isNotEmpty()) {
                val nfcEquipment = NfcEquipment(nfcEquipmentId, equipmentId,
                        nfcName.trim(), nfcEquipmentCode, true)
                saveNfcEquipment(nfcEquipment)
            } else viewState.showSnackbar(R.string.nfc_name_required)
        } else viewState.showSnackbar(R.string.length_too_long)
    }

    private fun saveNfcEquipment(nfcEquipment: NfcEquipment) {
        viewState.showProgress()
        subscriptions.add(interactor
                .saveNfcEquipment(nfcEquipment)
                .subscribe({
                    viewState.hideProgress()
                    viewState.closeFragment(getPopToScreen())
                }, {
                    viewState.hideProgress()
                    viewState.showToast(R.string.error_message)

                    viewState.closeFragment(getPopToScreen())
                    Timber.e(it)
                }))
    }

    private fun getPopToScreen(): Int {
        return when (equipmentScreen) {
            Constants.ROOT_EQUIPMENT_SCREEN -> R.id.rootEquipmentFragment
            Constants.NESTED_EQUIPMENT_SCREEN -> R.id.nestedEquipmentFragment
            else -> throw IllegalArgumentException("Unknown equipment screen: $equipmentScreen")
        }
    }
}