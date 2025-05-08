package com.acroninspector.app.presentation.fragment.defects.defectlogs

import com.acroninspector.app.R
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractor
import com.acroninspector.app.presentation.fragment.defects.DefectsBasePresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class DefectLogsPresenter(
        defectLogInteractor: DefectLogInteractor
) : DefectsBasePresenter<DefectLogsView>(defectLogInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        defaultEmptyStateMessage = R.string.empty_state_defects

        loadDefects()
    }

    fun refreshDefects() {
        if (defectLogs.isEmpty()) {
            loadDefects()
        } else viewState.hideProgress()
    }
}