package com.acroninspector.app.presentation.fragment.defectparameters.defectname

import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.domain.interactors.defectname.DefectNameInteractor
import com.acroninspector.app.presentation.fragment.defectparameters.DefectParametersPresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class DefectNamePresenter(
        private val defectNameInteractor: DefectNameInteractor
) : DefectParametersPresenter<DefectNameView>() {

    private var defectNames: List<DisplayDefect> = ArrayList()

    private val showDefectNamesConsumer: ((it: List<DisplayDefect>) -> Unit) = {
        handleEmptyState(it)
        viewState.hideProgress()
        viewState.updateDefectNames(it)

        defectNames = it
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadDefectNames()
    }

    private fun loadDefectNames() {
        viewState.showProgress()
        subscriptions.add(defectNameInteractor
                .getAllDefectNames(equipmentClassId)
                .subscribe(showDefectNamesConsumer, showErrorConsumer))
    }

    fun searchDefectNames(query: String) {
        viewState.showProgress()
        subscriptions.add(defectNameInteractor
                .getSearchedDefectNames(query, equipmentClassId)
                .subscribe(showDefectNamesConsumer, showErrorConsumer))
    }

    fun onDefectNameClicked(position: Int) {
        val defectNameId = defectNames[position].id
        viewState.passDefectNameId(defectNameId)
    }

    private fun handleEmptyState(defects: List<DisplayDefect>) {
        if (defects.isEmpty()) {
            viewState.showEmptyState()
        } else viewState.hideEmptyState()
    }
}