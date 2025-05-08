package com.acroninspector.app.presentation.fragment.taskdetails.bypass

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.KEY_ROUTE_OBJECT
import com.acroninspector.app.common.constants.Constants.KEY_TASK_ID
import com.acroninspector.app.common.constants.Constants.KEY_TASK_OBJECT
import com.acroninspector.app.common.constants.Constants.KEY_TASK_STATUS
import com.acroninspector.app.common.constants.Constants.UNANSWERED_LISTS
import com.acroninspector.app.common.constants.Constants.UNSCANNED_NFC_MARKS
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_ANOTHER_IN_PROGRESS
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_COMPLETED
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractor
import com.acroninspector.app.presentation.dialog.HasDefectsDialog
import com.acroninspector.app.presentation.dialog.UnfinishedTaskDialog
import com.acroninspector.app.presentation.fragment.taskdetails.TaskDetailsPresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber
import java.util.Calendar

@InjectViewState
class TaskDetailsByPassPresenter(
    private val taskDetailsInteractor: TaskDetailsInteractor,
    private val scanNfcInteractor: ScanNfcInteractor
) : TaskDetailsPresenter<TaskDetailsByPassView>(taskDetailsInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadRoutes()
        loadTask()
        checkExecutor()
    }

    private fun checkExecutor() {
        val executorId = taskDetailsInteractor.getExecutorId()
        when {
            taskExecutorId == executorId -> viewState.showRouteButton()
            taskStatus == TASK_STATUS_COMPLETED -> viewState.showRouteButton()
            else -> viewState.hideRouteButton()
        }
    }

    fun routeButtonClicked() {
        if (taskStatus == DatabaseConstants.TASK_STATUS_NEW) {
            viewState.showStartRouteDialog()
        } else handleFinishRoute()
    }

    private fun handleFinishRoute() {
        if (routes.isNotEmpty()) {
            viewState.showProgress()
            subscriptions.add(
                taskDetailsInteractor
                    .getUnansweredLists(routes)
                    .subscribe({ unansweredLists ->
                        viewState.hideProgress()

                        if (unansweredLists.isNotEmpty()) {
                            viewState.showUnfinishedTaskDialog(UNANSWERED_LISTS, unansweredLists)
                        } else checkAnsweredNfcMarks()
                    }, { viewState.hideProgress() })
            )
        }
    }

    fun checkAnsweredNfcMarks() {
        viewState.showProgress()
        subscriptions.add(
            taskDetailsInteractor
                .getUnansweredNfcMarks(routes)
                .subscribe({ unscannedNfcMarks ->
                    viewState.hideProgress()

                    if (unscannedNfcMarks.isNotEmpty()) {
                        viewState.showUnfinishedTaskDialog(UNSCANNED_NFC_MARKS, unscannedNfcMarks)
                    } else onFinishRouteClicked()
                }, { viewState.hideProgress() })
        )
    }

    fun onRouteClicked(childFragmentManager: FragmentManager, position: Int) {
        if ((taskStatus != DatabaseConstants.TASK_STATUS_NEW &&
                taskStatus != Constants.DEFAULT_INVALID_STATUS && taskStatus != TASK_STATUS_ANOTHER_IN_PROGRESS)
        ) {
            val args = Bundle()
            args.apply {
                putInt(KEY_TASK_ID, taskId)
                putInt(KEY_TASK_STATUS, taskStatus)
                putParcelable(KEY_ROUTE_OBJECT, routes[position])
            }

            val hasAnswers = hasAnswers(position)
            if (hasAnswers) {
                viewState.openQuestionsFragment(args)
            } else {
                HasDefectsDialog {
                    if (it) {
                        viewState.openQuestionsFragment(args)
                    } else {
                        hasNoDefectsForEquipment(position)
                    }
                }.show(childFragmentManager, UnfinishedTaskDialog.TAG)
                childFragmentManager.executePendingTransactions()
            }
        } else viewState.showSnackbar(R.string.task_new_route_clicked_error_message)
    }

    fun onRouteAttachmentsClicked(position: Int) {
        val args = Bundle()
        args.apply {
            putInt(Constants.KEY_ENTITY_ID, routes[position].id)
            putInt(Constants.KEY_ENTITY_TYPE, Constants.ENTITY_ROUTE)
            putBoolean(Constants.KEY_ENABLED_EDITING, false)
        }

        viewState.openMediaFilesFragment(args)
    }

    fun onRouteDefectsClicked(position: Int) {
        val args = Bundle()
        args.apply {
            putInt(Constants.KEY_ENTITY_ID, routes[position].id)
            putInt(Constants.KEY_ENTITY_TYPE, Constants.ENTITY_ROUTE)
        }

        viewState.openDefectsFragment(args)
    }

    fun onNfcScanned(childFragmentManager: FragmentManager, nfcCode: String) {
        viewState.showProgress()
        subscriptions.add(
            scanNfcInteractor
                .getRouteIdByNfcCode(nfcCode)
                .subscribe({ routeIds ->
                    val position = getRoutePositionById(routeIds)
                    if (position != -1) {
                        updateNfcRouteTime(
                            childFragmentManager = childFragmentManager,
                            nfcCode,
                            position
                        )
                    } else {
                        openEquipmentDialog(nfcCode)
                    }
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }, { openEquipmentDialog(nfcCode) })
        )
    }

    private fun openEquipmentDialog(nfcCode: String) {
        subscriptions.add(
            scanNfcInteractor
                .getEquipmentIdByNfcCode(nfcCode)
                .subscribe({ equipmentId ->
                    viewState.hideProgress()
                    viewState.showEquipmentByPassDialog(equipmentId)
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }, {
                    viewState.hideProgress()
                    viewState.showErrorDialog(
                        R.string.error,
                        R.string.scanned_nfc_tag_that_no_in_database, nfcCode
                    )
                })
        )
    }

    fun onDefectListClicked(equipmentId: Int) {
        val args = Bundle()
        args.apply {
            putInt(Constants.KEY_ENTITY_ID, equipmentId)
            putInt(Constants.KEY_ENTITY_TYPE, Constants.ENTITY_EQUIPMENT)
        }

        viewState.openDefectsFragment(args)
    }

    fun onRegisterDefectClicked(equipmentId: Int) {
        viewState.openRegisterDefectFragment(equipmentId)
    }

    private fun getRoutePositionById(routeIds: List<Int>): Int {
        routeIds.forEach { routeId ->
            for ((position, route) in routes.withIndex()) {
                if (route.id == routeId) {
                    return position
                }
            }
        }
        return -1
    }

    private fun updateNfcRouteTime(
        childFragmentManager: FragmentManager,
        nfcCode: String,
        routePosition: Int
    ) {
        val timeInMills = Calendar.getInstance().timeInMillis
        val dateScan = DateUtil.convertLongDateToString(timeInMills)
        val routeId = routes[routePosition].id

        subscriptions.add(
            scanNfcInteractor
                .updateNfcTime(routeId, nfcCode, dateScan, taskStatus)
                .subscribe({
                    viewState.hideProgress()
                    onRouteClicked(childFragmentManager = childFragmentManager, routePosition)
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                })
        )
    }

    fun onStartRouteClicked() {
        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val actualStartDate = DateUtil.convertLongDateToString(currentTimeInMills)

        subscriptions.add(
            taskDetailsInteractor
                .startRoute(taskId, DatabaseConstants.TASK_STATUS_IN_PROGRESS, actualStartDate)
                .subscribe({
                    taskStatus = DatabaseConstants.TASK_STATUS_IN_PROGRESS
                }, {
                    viewState.showSnackbar(R.string.error_message)
                    Timber.e(it)
                })
        )
    }

    fun onFinishRouteClicked() {
        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val actualEndDate = DateUtil.convertLongDateToString(currentTimeInMills)

        viewState.showProgress()
        subscriptions.add(
            taskDetailsInteractor
                .finishRoute(taskId, TASK_STATUS_COMPLETED, actualEndDate)
                .subscribe({
                    viewState.hideProgress()
                    taskStatus = TASK_STATUS_COMPLETED
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                })
        )
    }

    fun onTaskCommentClicked(task: DisplayTask) {
        if (taskId != DEFAULT_INVALID_ID && taskStatus != Constants.DEFAULT_INVALID_STATUS) {
            val args = Bundle()
            args.apply {
                putInt(KEY_TASK_ID, taskId)
                putInt(KEY_TASK_STATUS, taskStatus)
                putParcelable(KEY_TASK_OBJECT, task)
            }

            viewState.openTaskCommentFragment(args)
        }
    }

    fun onTaskAttachmentsClicked() {
        val args = Bundle()
        args.apply {
            putInt(Constants.KEY_ENTITY_ID, taskId)
            putInt(Constants.KEY_ENTITY_TYPE, Constants.ENTITY_TASK)
            putBoolean(Constants.KEY_ENABLED_EDITING, false)
        }

        viewState.openMediaFilesFragment(args)
    }

    fun showRouteDeffects() {
        val idsList = routes.map { it.equipmentId }.joinToString(separator = ",")
        val args = Bundle()
        args.apply {
            putString(Constants.KEY_ENTITY_ID_LIST, idsList)
            putInt(Constants.KEY_ENTITY_TYPE, Constants.ENTITY_EQUIPMENT_LIST)
            putBoolean(Constants.KEY_COULD_EDIT, false)
        }

        viewState.openRouteDefectsFragment(args)
    }
}