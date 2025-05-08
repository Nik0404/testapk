package com.acroninspector.app.presentation.fragment.defectdetails

import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.ENTITY_CHECK_LIST
import com.acroninspector.app.common.constants.Constants.ENTITY_DEFECT_LOG
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.interactors.defectdetails.DefectDetailsInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber

@InjectViewState
class DefectDetailsPresenter(
        private val interactor: DefectDetailsInteractor
) : BasePresenter<DefectDetailsView>() {

    var localDefectId = DEFAULT_INVALID_ID

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadLocalDefect()
    }

    private fun loadLocalDefect() {
        if (localDefectId != DEFAULT_INVALID_ID) {
            subscriptions.add(interactor
                    .getDisplayDefectById(localDefectId)
                    .subscribe({ displayDefectLog ->
                        viewState.setDefect(displayDefectLog)
                        viewState.setToolbarTitle(displayDefectLog.defectName)
                    }, { Timber.e(it) }))
        }
    }

    fun onEditDefectClicked(defect: DisplayDefectLog) {
        val entityType = getEntityType(defect)

        viewState.openRegisterDefectFragment(defect.id, entityType)
    }

    fun onAttachmentsClicked(defect: DisplayDefectLog) {
        val entityType = getEntityType(defect)

        val entityId = if (entityType == ENTITY_CHECK_LIST) {
            defect.checkListId
        } else defect.id

        viewState.openMediaFilesFragment(entityId, entityType)
    }

    private fun getEntityType(defect: DisplayDefectLog): Int {
        return if (defect.checkListId != DEFAULT_INVALID_ID) {
            ENTITY_CHECK_LIST
        } else ENTITY_DEFECT_LOG
    }
}