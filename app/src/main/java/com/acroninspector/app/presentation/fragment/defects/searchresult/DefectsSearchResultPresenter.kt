package com.acroninspector.app.presentation.fragment.defects.searchresult

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID_LIST
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_TYPE
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractor
import com.acroninspector.app.presentation.fragment.defects.DefectsBasePresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class DefectsSearchResultPresenter(
    defectLogInteractor: DefectLogInteractor
) : DefectsBasePresenter<DefectsSearchResultView>(defectLogInteractor) {

    var searchQuery = ""

    var entityId = DEFAULT_INVALID_ID
    var entityType = DEFAULT_INVALID_TYPE
    var entityIdList = DEFAULT_INVALID_ID_LIST
    var defectCouldEdit = true

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        handleDefectsLoading()
    }

    fun handleDefectsLoading() {
        when {
            searchQuery.isNotEmpty() && entityId == DEFAULT_INVALID_ID -> {
                defaultEmptyStateMessage = R.string.empty_state_search

                viewState.setSearchEmptyState(defaultEmptyStateMessage)
                loadDefects(searchQuery)
            }

            entityIdList.isNotEmpty() -> {

//                viewState.setEquipmentEmptyState(defaultEmptyStateMessage)
                loadDefectsOnEqList(entityIdList)
            }

            else -> {
                defaultEmptyStateMessage = R.string.empty_state_defects_history

                viewState.setEquipmentEmptyState(defaultEmptyStateMessage)
                loadDefects(entityId, entityType)
            }
        }
    }

    fun refreshDefects() {
        if (defectLogs.isEmpty()) {
            handleDefectsLoading()
        } else viewState.hideProgress()
    }
}