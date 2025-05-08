package com.acroninspector.app.presentation.fragment.registerdefect

import androidx.room.EmptyResultSetException
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.CRITICALITY_NO
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_TYPE
import com.acroninspector.app.common.constants.Constants.ENTITY_CHECK_LIST
import com.acroninspector.app.common.constants.Constants.ENTITY_DEFECT_LOG
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.interactors.registerdefect.RegisterDefectInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Flowable
import timber.log.Timber
import java.util.*

@InjectViewState
class RegisterDefectPresenter(
        private val interactor: RegisterDefectInteractor
) : BasePresenter<RegisterDefectView>() {

    private var isCreatingDefect = true

    var entityType = DEFAULT_INVALID_TYPE

    var localDefectId = DEFAULT_INVALID_ID
    var taskId = DEFAULT_INVALID_ID
    var equipmentId = DEFAULT_INVALID_ID
    var checkListId = DEFAULT_INVALID_ID
    var defectNameId = DEFAULT_INVALID_ID
    var defectCauseId = DEFAULT_INVALID_ID

    private var equipmentClassId = DEFAULT_INVALID_ID

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        prepareLocalDefect()
    }

    private fun prepareLocalDefect() {
        if (localDefectId == DEFAULT_INVALID_ID) {
            // Creating defect
            getLocalDefectId()
            getEquipment()
        } else {
            // Editing defect
            isCreatingDefect = false
            getLocalDefect(localDefectId)
        }
    }

    private fun getLocalDefect(localDefectId: Int) {
        viewState.showProgress()
        subscriptions.add(interactor
                .getLocalDefectById(localDefectId)
                .subscribe({ localDefect ->
                    viewState.hideProgress()

                    if (localDefect.criticality != CRITICALITY_NO) {
                        viewState.setCriticality(localDefect.criticality)
                    }
                    viewState.setComment(localDefect.comment)

                    taskId = localDefect.taskId
                    equipmentId = localDefect.equipmentId
                    checkListId = localDefect.checkListId
                    defectNameId = localDefect.defectNameId
                    defectCauseId = localDefect.defectCauseId

                    getEquipment()
                    getAttachmentsCount()
                    getDefectName(localDefect.defectNameId)
                    getDefectCause(localDefect.defectCauseId)
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }))
    }

    private fun getEquipment() {
        if (equipmentId != DEFAULT_INVALID_ID) {
            subscriptions.add(interactor
                    .getEquipmentById(equipmentId)
                    .subscribe({
                        equipmentClassId = it.equipmentClassId
                        viewState.setEquipment(it.name, it.code)
                    }, {
                        viewState.showSnackbar(R.string.error_message)

                        Timber.e(it)
                    })
            )
        } else viewState.showSnackbar(R.string.error_message)
    }

    private fun getLocalDefectId() {
        subscriptions.add(interactor
                .getLocalDefectId()
                .subscribe({ localDefectId ->
                    prepareLocalDefectId(localDefectId)
                }, {
                    if (it is EmptyResultSetException) {
                        prepareLocalDefectId(1)
                    } else {
                        viewState.showToast(R.string.error_message)
                        viewState.closeFragment()

                        Timber.e(it)
                    }
                })
        )
    }

    private fun prepareLocalDefectId(localDefectId: Int) {
        this.localDefectId = localDefectId

        getAttachmentsCount()
    }

    private fun getAttachmentsCount() {
        subscriptions.add(handleGettingAttachmentsCount()
                .subscribe({ count ->
                    viewState.setAttachmentsCount(count)
                }, { Timber.e(it) })
        )
    }

    private fun handleGettingAttachmentsCount(): Flowable<Int> {
        return when (entityType) {
            ENTITY_DEFECT_LOG -> interactor.getDefectAttachmentsCount(localDefectId)
            ENTITY_CHECK_LIST -> interactor.getCheckListAttachmentsCount(checkListId)
            else -> throw IllegalArgumentException("Unknown entity type: $entityType")
        }
    }

    fun getDefectCause(defectCauseId: Int) {
        this.defectCauseId = defectCauseId

        subscriptions.add(interactor
                .getDefectCauseById(defectCauseId)
                .subscribe({
                    viewState.setDefectCauseName(it.fullName)
                }, { Timber.e(it) })
        )
    }

    fun getDefectName(defectNameId: Int) {
        this.defectNameId = defectNameId

        subscriptions.add(interactor
                .getDefectNameById(defectNameId)
                .subscribe({
                    viewState.setDefectName(it.fullName)
                }, { Timber.e(it) })
        )
    }

    fun onSaveClicked(nullableCriticality: Int?, comment: String) {
        val criticality = nullableCriticality ?: CRITICALITY_NO
        val timeInMills = Calendar.getInstance().timeInMillis
        val dateCreation = DateUtil.convertLongDateToString(timeInMills)

        val defect = LocalDefect(checkListId, equipmentId, taskId, defectCauseId,
                defectNameId, comment, criticality, dateCreation)
        defect.id = localDefectId

        saveDefect(defect)
    }

    private fun saveDefect(localDefect: LocalDefect) {
        viewState.showProgress()
        subscriptions.add(interactor
                .insertDefect(localDefect)
                .subscribe({
                    viewState.hideProgress()
                    viewState.closeFragment()
                }, {
                    viewState.hideProgress()
                    viewState.showToast(R.string.error_message)

                    viewState.closeFragment()
                    Timber.e(it)
                })
        )
    }

    fun onCriticalitySelected(criticality: Int) {
        viewState.setCriticality(criticality)
    }

    fun onCardDefectCriticalityClicked() {
        viewState.showCriticalityDialog()
    }

    fun onCardDefectNameClicked() {
        viewState.openDefectNameFragment(equipmentClassId)
    }

    fun onCardDefectCauseNameClicked() {
        if (defectNameId != DEFAULT_INVALID_ID) {
            viewState.openDefectCauseFragment(defectNameId, equipmentClassId)
        } else viewState.showErrorDialog()
    }

    fun onCardDefectCommentClicked() {
        viewState.openCommentFragment(localDefectId)
    }

    fun onCommentChanged(comment: String) {
        viewState.setComment(comment)
    }

    fun onMediaFilesClicked() {
        when (entityType) {
            ENTITY_DEFECT_LOG -> viewState.openMediaFilesFragment(localDefectId, ENTITY_DEFECT_LOG,
                    isCreatingDefect)
            ENTITY_CHECK_LIST -> viewState.openMediaFilesFragment(checkListId, ENTITY_CHECK_LIST)
        }
    }

    fun resetDefectCause() {
        viewState.resetDefectCauseName()
        defectCauseId = DEFAULT_INVALID_ID
    }

    fun onBackPressed() {
        if (isCreatingDefect) {
            subscriptions.add(interactor
                    .deleteMediaFilesByDefectLogId(localDefectId)
                    .subscribe({
                        viewState.closeFragment()
                    }, {
                        Timber.e(it)
                        viewState.closeFragment()
                    }))
        } else {
            viewState.closeFragment()
        }
    }
}