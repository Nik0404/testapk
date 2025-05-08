package com.acroninspector.app.presentation.fragment.defectparameters.defectcause

import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import com.acroninspector.app.domain.interactors.defectcause.DefectCauseInteractor
import com.acroninspector.app.presentation.fragment.defectparameters.DefectParametersPresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class DefectCausePresenter(
        private val defectCauseInteractor: DefectCauseInteractor
) : DefectParametersPresenter<DefectCauseView>() {

    private var defectCauses: List<DisplayDefectCause> = ArrayList()

    var defectNameId = DEFAULT_INVALID_ID

    private val showDefectCausesConsumer: ((it: List<DisplayDefectCause>) -> Unit) = {
        handleEmptyState(it)
        viewState.hideProgress()
        viewState.updateDefectCauses(it)

        defectCauses = it
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadDefectCauses()
    }

    private fun loadDefectCauses() {
        viewState.showProgress()
        subscriptions.add(defectCauseInteractor
                .getAllDefectCauses(defectNameId, equipmentClassId)
                .subscribe(showDefectCausesConsumer, showErrorConsumer))
    }

    fun onDefectCauseClicked(position: Int) {
        val defectCauseId = defectCauses[position].id
        viewState.passDefectCauseId(defectCauseId)
    }

    private fun handleEmptyState(defectCauses: List<DisplayDefectCause>) {
        if (defectCauses.isEmpty()) {
            viewState.showEmptyState()
        } else viewState.hideEmptyState()
    }
}