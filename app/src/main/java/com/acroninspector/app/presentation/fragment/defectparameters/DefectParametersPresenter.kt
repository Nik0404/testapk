package com.acroninspector.app.presentation.fragment.defectparameters

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.presentation.mvp.BasePresenter
import timber.log.Timber

abstract class DefectParametersPresenter<ViewState : DefectParametersView>
    : BasePresenter<ViewState>() {

    var equipmentClassId = Constants.DEFAULT_INVALID_ID

    protected val showErrorConsumer: ((it: Throwable) -> Unit) = {
        viewState.hideProgress()
        viewState.showSnackbar(R.string.error_message)

        Timber.e(it)
    }
}