package com.acroninspector.app.presentation.fragment.nfc.definenfc

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.domain.interactors.nfc.NfcInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber

@InjectViewState
class DefineNfcPresenter(
        private val interactor: NfcInteractor
) : BasePresenter<DefineNfcView>() {

    var nfcEquipmentId = DEFAULT_INVALID_ID
    var nfcEquipmentCode = ""

    fun onSaveClicked() = viewState.openNfcNameFragment(nfcEquipmentId, nfcEquipmentCode)

    fun onDropClicked() = viewState.dropNfcMark()

    fun onNfcScanned(nfcCode: String) {
        subscriptions.add(
            interactor.checkIfNfcExists(nfcCode)
                .subscribe({ maxId ->
                    if (maxId == DEFAULT_INVALID_ID) {
                        viewState.showErrorDialog()
                    } else {
                        nfcEquipmentId = maxId + 1
                        nfcEquipmentCode = nfcCode

                        viewState.setNfcMark(nfcCode)
                    }
                }, {
                    viewState.showSnackbar(R.string.error_message)
                    Timber.e(it)
                }))
    }
}