package com.acroninspector.app.presentation.fragment.questions

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.ANSWER_NO
import com.acroninspector.app.common.constants.Constants.ANSWER_YES
import com.acroninspector.app.common.constants.Constants.CRITICALITY_NO
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_STATUS
import com.acroninspector.app.common.constants.Constants.ENTITY_CHECK_LIST
import com.acroninspector.app.common.constants.Constants.ENTITY_EQUIPMENT
import com.acroninspector.app.common.constants.Constants.ENTITY_ROUTE
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.constants.DatabaseConstants.TYPE_ANSWER_BUTTONS
import com.acroninspector.app.common.constants.DatabaseConstants.TYPE_ANSWER_TEMPLATE
import com.acroninspector.app.common.constants.DatabaseConstants.TYPE_ANSWER_VALUE
import com.acroninspector.app.common.extension.isValueAnswerIsDefect
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.interactors.checklist.CheckListInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber
import java.util.Calendar

@InjectViewState
class QuestionsPresenter(
        private val checkListInteractor: CheckListInteractor,
        private val scanNfcInteractor: ScanNfcInteractor
) : BasePresenter<QuestionsView>() {

    private var questions: List<DisplayQuestion> = ArrayList()

    var routeId = DEFAULT_INVALID_ID
    var taskId = DEFAULT_INVALID_ID
    var equipmentId = DEFAULT_INVALID_ID

    var taskStatus = DEFAULT_INVALID_STATUS

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadQuestions()
        loadRoute()
    }

    private fun loadQuestions() {
        if (taskId != DEFAULT_INVALID_ID && routeId != DEFAULT_INVALID_ID) {
            viewState.showProgress()
            subscriptions.add(checkListInteractor
                    .getQuestionsByRouteId(routeId)
                    .subscribe({
                        viewState.hideProgress()
                        viewState.updateQuestions(it)

                        questions = it
                    }, {
                        viewState.hideProgress()
                        viewState.showSnackbar(R.string.error_message)

                        Timber.e(it)
                    }))
        } else viewState.showSnackbar(R.string.error_message)
    }

    private fun loadRoute() {
        if (routeId != DEFAULT_INVALID_ID) {
            subscriptions.add(checkListInteractor
                    .getRouteById(routeId)
                    .subscribe({
                        viewState.setRoute(it)
                    }, { Timber.e(it) }))
        }
    }

    fun updateAnswer(position: Int, answer: String) {
        val questionId = questions[position].id

        val answerDate = getAnswerDate(answer)
        val isDefect = answerIsDefect(position, answer)

        subscriptions.add(checkListInteractor
                .updateAnswer(routeId, questionId, answer, answerDate, isDefect)
                .subscribe({ }, { Timber.e(it) }, {
                    if (isDefect) {
                        createDefectLog(position, answerDate)
                    }
                }))
    }

    private fun getAnswerDate(answer: String): String {
        return if (answer.isNotEmpty()) {
            DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)
        } else ""
    }

    private fun answerIsDefect(position: Int, answer: String): Boolean {
        return when (val questionType = questions[position].typeAnswer) {
            TYPE_ANSWER_BUTTONS -> answerYesNoIsDefect(answer)
            TYPE_ANSWER_VALUE -> answerValueIsDefect(position, answer)
            TYPE_ANSWER_TEMPLATE -> answerTemplateIsDefect(position, answer)
            else -> throw IllegalArgumentException("Unknow question type: $questionType")
        }
    }

    private fun answerYesNoIsDefect(answer: String): Boolean {
        return when (answer) {
            ANSWER_YES -> false
            ANSWER_NO -> true
            else -> false
        }
    }

    private fun answerValueIsDefect(position: Int, answer: String): Boolean {
        val minValue = questions[position].minValue
        val maxValue = questions[position].maxValue
        return answer.isValueAnswerIsDefect(minValue, maxValue)
    }

    private fun answerTemplateIsDefect(position: Int, answer: String): Boolean {
        var isDefect = false
        questions[position].answersByDefault.forEach { displayAnswer ->
            if (displayAnswer.answerText == answer) {
                isDefect = displayAnswer.isDefect
            }
        }
        return isDefect
    }

    private fun createDefectLog(position: Int, answerDate: String) {
        val localDefect = LocalDefect(
                checkListId = questions[position].id,
                equipmentId = equipmentId,
                taskId = taskId,
                defectCauseId = DEFAULT_INVALID_ID,
                defectNameId = DEFAULT_INVALID_ID,
                comment = questions[position].comment,
                criticality = CRITICALITY_NO,
                dateCreation = answerDate
        )

        subscriptions.add(checkListInteractor
                .insertLocalDefect(localDefect)
                .subscribe({ }, { Timber.e(it) }))
    }

    fun onClickMenu() {
        viewState.hideKeyboard()
    }

    fun onClickAttachments(position: Int) {
        val enableEditing = taskStatus == DatabaseConstants.TASK_STATUS_IN_PROGRESS

        viewState.hideKeyboard()
        viewState.openMediaFilesFragment(questions[position].id, ENTITY_CHECK_LIST, enableEditing)
    }

    private var changeCommentQuestionId = 0

    fun onClickComment(position: Int) {
        changeCommentQuestionId = questions[position].id
        when (taskStatus) {
            DatabaseConstants.TASK_STATUS_IN_PROGRESS -> openCommentFragment(position, true)
            DatabaseConstants.TASK_STATUS_COMPLETED -> openCommentFragment(position, false)
        }
    }

    fun onCommentChanged(comment: String) {
        subscriptions.add(checkListInteractor
                .updateQuestionComment(changeCommentQuestionId, comment)
                .subscribe({ changeCommentQuestionId = 0 }, {
                    changeCommentQuestionId = 0

                    viewState.showSnackbar(R.string.error_message)
                    Timber.e(it)
                }))
    }

    fun onClickEditDefect(position: Int) {
        when (taskStatus) {
            DatabaseConstants.TASK_STATUS_IN_PROGRESS -> openRegisterDefectFragment(position)
            DatabaseConstants.TASK_STATUS_COMPLETED -> openDefectDetailsFragment(position)
        }
    }

    fun onClickSelectAnswer(position: Int, answerSelected: (String) -> Unit) {
        val answers = questions[position].answersByDefault
        viewState.showSelectAnswerDialog(answers, position, answerSelected)
    }

    fun onRouteAttachmentsClicked() {
        viewState.openMediaFilesFragment(routeId, ENTITY_ROUTE)
    }

    fun onRouteDefectsClicked() {
        viewState.openDefectsFragment(routeId, ENTITY_ROUTE)
    }

    fun onNfcScanned(nfcCode: String) {
        viewState.showProgress()
        subscriptions.add(scanNfcInteractor
                .getRouteByNfcCode(taskId, nfcCode)
                .subscribe({ route ->
                    if (route.taskId == taskId) {
                        updateNfcRouteTime(nfcCode, route)
                    } else openEquipmentDialog(nfcCode)
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }, { openEquipmentDialog(nfcCode) }))
    }

    private fun updateNfcRouteTime(nfcCode: String, route: DisplayRoute) {
        val timeInMills = Calendar.getInstance().timeInMillis
        val dateScan = DateUtil.convertLongDateToString(timeInMills)

        subscriptions.add(scanNfcInteractor
                .updateNfcTime(route.id, nfcCode, dateScan, taskStatus)
                .subscribe({
                    viewState.hideProgress()
                    if (route.id != routeId) {
                        viewState.openQuestionsFragment(taskId, taskStatus, route)
                    }
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }))
    }

    private fun openEquipmentDialog(nfcCode: String) {
        subscriptions.add(scanNfcInteractor
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
                    viewState.showErrorDialog(R.string.error,
                            R.string.scanned_nfc_tag_that_no_in_database, nfcCode)
                }))
    }

    fun onRegisterDefectClicked(equipmentId: Int) {
        viewState.openRegisterDefectFragment(equipmentId)
    }

    fun onDefectListClicked(equipmentId: Int) {
        viewState.openDefectsFragment(equipmentId, ENTITY_EQUIPMENT)
    }

    private fun openCommentFragment(position: Int, enableEditing: Boolean) {
        val comment = questions[position].comment
        viewState.openCommentFragment(comment, enableEditing)
    }

    private fun openDefectDetailsFragment(position: Int) {
        val questionId = questions[position].id

        viewState.showProgress()
        subscriptions.add(checkListInteractor
                .getDefectLogByCheckListId(questionId)
                .subscribe({ defectLog ->
                    viewState.hideProgress()
                    viewState.openDefectDetailsFragment(defectLog)
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }, { viewState.hideProgress() }))
    }

    private fun openRegisterDefectFragment(position: Int) {
        val questionId = questions[position].id

        viewState.showProgress()
        subscriptions.add(checkListInteractor
                .getDefectLogByCheckListId(questionId)
                .subscribe({ defectLog ->
                    viewState.hideProgress()
                    openRegisterDefectFragment(defectLog.id, questionId)
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }, {
                    viewState.hideProgress()
                    openRegisterDefectFragment(DEFAULT_INVALID_ID, questionId)
                })
        )
    }

    private fun openRegisterDefectFragment(defectLogId: Int, checkListId: Int) {
        viewState.openRegisterDefectFragment(defectLogId, checkListId, equipmentId, taskId)
    }

    fun showEquipmentDeffect() {
        viewState.openEquipmentDeffects(equipmentId = equipmentId)

    }
}