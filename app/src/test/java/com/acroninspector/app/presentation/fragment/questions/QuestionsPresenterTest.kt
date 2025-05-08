package com.acroninspector.app.presentation.fragment.questions

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_COMPLETED
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_IN_PROGRESS
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.interactors.checklist.CheckListInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.util.*

@RunWith(PowerMockRunner::class)
class QuestionsPresenterTest {

    private lateinit var presenter: QuestionsPresenter

    @Mock
    lateinit var interactor: CheckListInteractor

    @Mock
    lateinit var scanNfcInteractor: ScanNfcInteractor

    @Mock
    lateinit var viewState: `QuestionsView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = QuestionsPresenter(interactor, scanNfcInteractor)
        presenter.setViewState(viewState)
    }

    private fun getFakeQuestions(): List<DisplayQuestion> {
        return arrayListOf(
                DisplayQuestion(0, 0, 0, "", 1, false, ""),
                DisplayQuestion(1, 1, 1, "", 1, false, ""),
                DisplayQuestion(2, 2, 2, "", 1, false, "")
        )
    }

    private fun getFakeDefect(): DisplayDefectLog {
        return DisplayDefectLog(0, "", "", "", 0, "")
    }

    private fun getFakeLocalDefect(): LocalDefect {
        return LocalDefect(0, 0, 0, DEFAULT_INVALID_ID,
                DEFAULT_INVALID_ID, "", 0, "test")
    }

    private fun getFakeRoute(): DisplayRoute {
        val route = DisplayRoute(0, 0, 0, 0, "", "",
                "", 5, 5, 0, 0, 0, 0)
        route.startDateActual = "test"
        route.endDateActual = "test"

        return route
    }

    private fun getFakeRoute(id: Int): DisplayRoute {
        val route = DisplayRoute(id, 0, 0, 0, "", "",
            "", 5, 5, 0, 0, 0, 0)
        route.startDateActual = "test"
        route.endDateActual = "test"

        return route
    }

    private fun getFakeRouteNotStarted(): DisplayRoute {
        return DisplayRoute(0, 0, 0, 0, "", "",
                "", 0, 0, 0, 0, 0, 0)
    }

    private fun getFakeRouteNotEnded(): DisplayRoute {
        val route = DisplayRoute(0, 0, 0, 0, "", "",
                "", 5, 4, 0, 0, 0, 0)
        route.startDateActual = "test"

        return route
    }

    @Test
    fun loadQuestions_IncorrectTaskId() {
        val methodName = "loadQuestions"

        presenter.taskId = DEFAULT_INVALID_ID
        presenter.routeId = 0

        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(viewState).showSnackbar(R.string.error_message)
        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
    }

    @Test
    fun loadQuestions_IncorrectRouteId() {
        val methodName = "loadQuestions"

        presenter.taskId = 0
        presenter.routeId = DEFAULT_INVALID_ID

        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(viewState).showSnackbar(R.string.error_message)
        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
    }

    @Test
    fun loadQuestions_IncorrectBoth() {
        val methodName = "loadQuestions"

        presenter.taskId = DEFAULT_INVALID_ID
        presenter.routeId = DEFAULT_INVALID_ID

        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(viewState).showSnackbar(R.string.error_message)
        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
    }

    @Test
    fun loadQuestions_Success() {
        val methodName = "loadQuestions"

        val routeId = 1
        val questions = getFakeQuestions()

        presenter.taskId = 0
        presenter.routeId = routeId

        `when`(interactor.getQuestionsByRouteId(routeId))
                .thenReturn(Flowable.just(questions))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getQuestionsByRouteId(routeId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateQuestions(questions)

        inOrder.verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadQuestions_Error() {
        val methodName = "loadQuestions"

        val routeId = 1

        presenter.taskId = 0
        presenter.routeId = routeId

        `when`(interactor.getQuestionsByRouteId(routeId))
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getQuestionsByRouteId(routeId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        inOrder.verify(viewState, never()).updateQuestions(ArgumentMatchers.anyList())
    }

    @Test
    fun onClickEditDefect_Status_In_Progress_HasDefect() {
        val questions = getFakeQuestions()
        val defect = getFakeDefect()
        val position = 1

        val equipmentId = 1
        val taskId = 1
        val checkListId = questions[position].id

        `when`(interactor.getDefectLogByCheckListId(checkListId))
                .thenReturn(Maybe.just(defect))

        Whitebox.setInternalState(presenter, "questions", questions)

        presenter.equipmentId = equipmentId
        presenter.taskId = taskId
        presenter.taskStatus = TASK_STATUS_IN_PROGRESS

        presenter.onClickEditDefect(position)

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDefectLogByCheckListId(checkListId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState)
                .openRegisterDefectFragment(defect.id, checkListId, equipmentId, taskId)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun onClickEditDefect_Status_InProgress_Empty() {
        val questions = getFakeQuestions()
        val position = 1

        val equipmentId = 1
        val taskId = 1
        val checkListId = questions[position].id

        `when`(interactor.getDefectLogByCheckListId(checkListId))
                .thenReturn(Maybe.empty())

        Whitebox.setInternalState(presenter, "questions", questions)

        presenter.equipmentId = equipmentId
        presenter.taskId = taskId
        presenter.taskStatus = TASK_STATUS_IN_PROGRESS
        presenter.onClickEditDefect(position)

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDefectLogByCheckListId(checkListId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState)
                .openRegisterDefectFragment(DEFAULT_INVALID_ID, checkListId, equipmentId, taskId)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun onClickEditDefect_Status_Completed_Empty() {
        val questions = getFakeQuestions()
        val position = 1

        val equipmentId = 1
        val taskId = 1
        val checkListId = questions[position].id

        `when`(interactor.getDefectLogByCheckListId(checkListId))
                .thenReturn(Maybe.empty())

        Whitebox.setInternalState(presenter, "questions", questions)

        presenter.equipmentId = equipmentId
        presenter.taskId = taskId
        presenter.taskStatus = TASK_STATUS_COMPLETED
        presenter.onClickEditDefect(position)

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDefectLogByCheckListId(checkListId)
        inOrder.verify(viewState).hideProgress()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).openRegisterDefectFragment(
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()
        )
    }

    @Test
    fun onClickEditDefect_Status_Completed_HasDefect() {
        val questions = getFakeQuestions()
        val defect = getFakeDefect()
        val position = 1

        val equipmentId = 1
        val taskId = 1
        val checkListId = questions[position].id

        `when`(interactor.getDefectLogByCheckListId(checkListId))
                .thenReturn(Maybe.just(defect))

        Whitebox.setInternalState(presenter, "questions", questions)

        presenter.equipmentId = equipmentId
        presenter.taskId = taskId
        presenter.taskStatus = TASK_STATUS_COMPLETED
        presenter.onClickEditDefect(position)

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDefectLogByCheckListId(checkListId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openDefectDetailsFragment(defect)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).openRegisterDefectFragment(
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()
        )
    }

    @Test
    fun onClickEditDefect_Failed() {
        val questions = getFakeQuestions()
        val position = 1

        val checkListId = questions[position].id

        `when`(interactor.getDefectLogByCheckListId(checkListId))
                .thenReturn(Maybe.error(Throwable()))

        Whitebox.setInternalState(presenter, "questions", questions)

        presenter.taskStatus = TASK_STATUS_COMPLETED
        presenter.onClickEditDefect(position)

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDefectLogByCheckListId(checkListId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).showSnackbar(R.string.register_defect_only_to_task_in_progress)
        verify(viewState, never()).openRegisterDefectFragment(
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()
        )
        verify(viewState, never()).openDefectDetailsFragment(ArgumentMatchers.any())
    }

    @Test
    fun onRouteAttachmentsClicked() {
        val routeId = 0

        presenter.routeId = routeId
        presenter.onRouteAttachmentsClicked()

        verify(viewState).openMediaFilesFragment(routeId, Constants.ENTITY_ROUTE)
    }

    @Test
    fun onRouteDefectsClicked() {
        val routeId = 0

        presenter.routeId = routeId
        presenter.onRouteDefectsClicked()

        verify(viewState).openDefectsFragment(routeId, Constants.ENTITY_ROUTE)
    }

    @Test
    fun onQuestionAttachmentsClicked() {
        val questions = getFakeQuestions()
        val position = 0

        Whitebox.setInternalState(presenter, "questions", questions)
        presenter.onClickAttachments(position)

        verify(viewState).openMediaFilesFragment(questions[position].id, Constants.ENTITY_CHECK_LIST)
    }

    @Test
    fun updateAnswer_Success_QuestionIsDefect() {
        val localDefect = getFakeLocalDefect()
        val questions = getFakeQuestions()
        val position = 0
        val questionId = 0
        val routeId = 0
        val taskId = 0
        val equipmentId = 0
        val answer = "0"
        val answerDate = DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)
        localDefect.dateCreation = answerDate
        val isDefect = true

        `when`(interactor.updateAnswer(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                .thenReturn(Maybe.empty())

        `when`(interactor.insertLocalDefect(localDefect))
                .thenReturn(Completable.complete())

        Whitebox.setInternalState(presenter, "questions", questions)
        Whitebox.setInternalState(presenter, "routeId", routeId)
        Whitebox.setInternalState(presenter, "taskId", taskId)
        Whitebox.setInternalState(presenter, "equipmentId", equipmentId)
        presenter.updateAnswer(position, answer)

        verify(interactor).updateAnswer(routeId, questionId, answer, answerDate, isDefect)
        verify(interactor).insertLocalDefect(localDefect)
    }

    @Test
    fun updateAnswer_Success_QuestionIsNotDefect() {
        val localDefect = getFakeLocalDefect()
        val questions = getFakeQuestions()
        val position = 0
        val questionId = 0
        val routeId = 0
        val answer = ""
        val answerDate = ""
        val isDefect = false

        `when`(interactor.updateAnswer(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                .thenReturn(Maybe.empty())

        Whitebox.setInternalState(presenter, "questions", questions)
        Whitebox.setInternalState(presenter, "routeId", routeId)
        presenter.updateAnswer(position, answer)

        verify(interactor).updateAnswer(routeId, questionId, answer, answerDate, isDefect)

        verify(interactor, never()).insertLocalDefect(localDefect)
    }

    @Test
    fun updateAnswer_Success_QuestionHasDefect() {
        val localDefect = getFakeLocalDefect()
        val questions = getFakeQuestions()
        val position = 0
        val questionId = 0
        val routeId = 0
        val answer = ""
        val answerDate = ""
        val isDefect = false

        `when`(interactor.updateAnswer(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                .thenReturn(Maybe.just(localDefect))

        Whitebox.setInternalState(presenter, "questions", questions)
        Whitebox.setInternalState(presenter, "routeId", routeId)
        presenter.updateAnswer(position, answer)

        verify(interactor).updateAnswer(routeId, questionId, answer, answerDate, isDefect)

        verify(interactor, never()).insertLocalDefect(localDefect)
    }

    @Test
    fun updateAnswer_Failed() {
        val localDefect = getFakeLocalDefect()
        val questions = getFakeQuestions()
        val position = 0
        val questionId = 0
        val routeId = 0
        val answer = ""
        val answerDate = ""
        val isDefect = false

        `when`(interactor.updateAnswer(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                .thenReturn(Maybe.error(Throwable()))

        Whitebox.setInternalState(presenter, "questions", questions)
        Whitebox.setInternalState(presenter, "routeId", routeId)
        presenter.updateAnswer(position, answer)

        verify(interactor).updateAnswer(routeId, questionId, answer, answerDate, isDefect)

        verify(interactor, never()).insertLocalDefect(localDefect)
    }

    @Test
    fun loadRoute_InvalidId() {
        val methodName = "loadRoute"

        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(interactor, never()).getRouteById(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadRoute_Success() {
        val methodName = "loadRoute"
        val route = getFakeRoute()

        `when`(interactor.getRouteById(route.id)).thenReturn(Flowable.just(route))

        presenter.routeId = route.id
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getRouteById(route.id)
        inOrder.verify(viewState).setRoute(route)
    }

    @Test
    fun loadRoute_Failed() {
        val methodName = "loadRoute"
        val route = getFakeRoute()

        `when`(interactor.getRouteById(route.id)).thenReturn(Flowable.error(Throwable()))

        presenter.routeId = route.id
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(interactor).getRouteById(route.id)

        verify(viewState, never()).setRoute(route)
    }

    @Test
    fun onNfcScanned_Success_SameTask() {
        val route = getFakeRoute()
        val nfcCode = "AA:AA:AA:AA:AA"

        val timeInMills = Calendar.getInstance().timeInMillis
        val dateScan = DateUtil.convertLongDateToString(timeInMills)

        `when`(scanNfcInteractor.getRouteByNfcCode(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
                .thenReturn(Maybe.just(route))

        `when`(scanNfcInteractor.updateNfcTime(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenReturn(Completable.complete())

        val routeId = route.id
        val taskId = route.taskId
        val taskStatus = TASK_STATUS_IN_PROGRESS

        presenter.taskId = taskId
        presenter.routeId = routeId
        presenter.taskStatus = taskStatus

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        presenter.onNfcScanned(nfcCode)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getRouteByNfcCode(taskId, nfcCode)
        inOrder.verify(scanNfcInteractor).updateNfcTime(routeId, nfcCode, dateScan, taskStatus)
        inOrder.verify(viewState).hideProgress()

        verify(scanNfcInteractor, never()).getEquipmentIdByNfcCode(ArgumentMatchers.anyString())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).openQuestionsFragment(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.any())
    }

    @Test
    fun onNfcScanned_Success_AnotherTask() {
        val equipmentId = 0
        val route = getFakeRoute()
        val nfcCode = "AA:AA:AA:AA:AA"

        `when`(scanNfcInteractor.getRouteByNfcCode(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
                .thenReturn(Maybe.just(route))

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(ArgumentMatchers.anyString()))
                .thenReturn(Maybe.just(equipmentId))

        val routeId = route.id
        val taskId = route.taskId + 1
        val taskStatus = TASK_STATUS_IN_PROGRESS

        presenter.taskId = taskId
        presenter.routeId = routeId
        presenter.taskStatus = taskStatus

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        presenter.onNfcScanned(nfcCode)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getRouteByNfcCode(taskId, nfcCode)
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEquipmentByPassDialog(equipmentId)

        verify(scanNfcInteractor, never()).updateNfcTime(
                ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).openQuestionsFragment(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.any())
    }

    @Test
    fun onNfcScanned_Failed() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val taskId = 1

        `when`(scanNfcInteractor.getRouteByNfcCode(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
                .thenReturn(Maybe.error(Throwable()))

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        Whitebox.setInternalState(presenter, "taskId", taskId)
        presenter.onNfcScanned(nfcCode)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getRouteByNfcCode(taskId, nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(scanNfcInteractor, never()).updateNfcTime(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        verify(viewState, never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
        verify(scanNfcInteractor, never()).getEquipmentIdByNfcCode(ArgumentMatchers.anyString())
        verify(viewState, never()).openQuestionsFragment(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.any())
    }

    @Test
    fun onNfcScanned_HasNotRoute() {
        val equipmentId = 0
        val nfcCode = "AA:AA:AA:AA:AA"
        val taskId = 1

        `when`(scanNfcInteractor.getRouteByNfcCode(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
                .thenReturn(Maybe.empty())

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(ArgumentMatchers.anyString()))
                .thenReturn(Maybe.just(equipmentId))

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        Whitebox.setInternalState(presenter, "taskId", taskId)
        presenter.onNfcScanned(nfcCode)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getRouteByNfcCode(taskId, nfcCode)
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEquipmentByPassDialog(equipmentId)

        verify(scanNfcInteractor, never()).updateNfcTime(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).openQuestionsFragment(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.any())
    }

    @Test
    fun openEquipmentDialog_HasEquipment() {
        val methodName = "openEquipmentDialog"
        val nfcCode = "AA:AA:AA:AA:AA"
        val equipmentId = 0

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.just(equipmentId))

        Whitebox.invokeMethod<Void>(presenter, methodName, nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEquipmentByPassDialog(equipmentId)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showErrorDialog(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
    }

    @Test
    fun openEquipmentDialog_Empty() {
        val methodName = "openEquipmentDialog"
        val nfcCode = "AA:AA:AA:AA:AA"

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.empty())

        Whitebox.invokeMethod<Void>(presenter, methodName, nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showErrorDialog(R.string.error,
                R.string.scanned_nfc_tag_that_no_in_database, nfcCode)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
    }

    @Test
    fun openEquipmentDialog_Failed() {
        val methodName = "openEquipmentDialog"
        val nfcCode = "AA:AA:AA:AA:AA"

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName, nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
        verify(viewState, never()).showErrorDialog(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
    }

    @Test
    fun updateNfcRouteTime_Success_SameRoute() {
        val methodName = "updateNfcRouteTime"

        val taskStatus = TASK_STATUS_IN_PROGRESS
        val nfcCode = "AA:AA:AA:AA:AA"
        val route = getFakeRoute()
        val routeId = route.id

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val dateScan = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(scanNfcInteractor.updateNfcTime(routeId, nfcCode, dateScan, taskStatus))
                .thenReturn(Completable.complete())

        presenter.taskStatus = taskStatus
        presenter.routeId = routeId
        Whitebox.invokeMethod<Void>(presenter, methodName, nfcCode, route)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(scanNfcInteractor).updateNfcTime(routeId, nfcCode, dateScan, taskStatus)
        inOrder.verify(viewState).hideProgress()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).openQuestionsFragment(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.any())
    }

    @Test
    fun updateNfcRouteTime_Success_AnotherRoute() {
        val methodName = "updateNfcRouteTime"

        val taskStatus = TASK_STATUS_IN_PROGRESS
        val nfcCode = "AA:AA:AA:AA:AA"

        val route = getFakeRoute()
        val routeId = route.id + 1

        val taskId = 0

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val dateScan = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(scanNfcInteractor.updateNfcTime(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenReturn(Completable.complete())

        presenter.taskId = taskId
        presenter.taskStatus = taskStatus
        presenter.routeId = routeId
        Whitebox.invokeMethod<Void>(presenter, methodName, nfcCode, route)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(scanNfcInteractor).updateNfcTime(route.id, nfcCode, dateScan, taskStatus)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openQuestionsFragment(taskId, taskStatus, route)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun updateNfcRouteTime_Failed() {
        val methodName = "updateNfcRouteTime"

        val taskStatus = TASK_STATUS_IN_PROGRESS
        val nfcCode = "AA:AA:AA:AA:AA"
        val route = getFakeRoute()

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val dateScan = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(scanNfcInteractor.updateNfcTime(route.id, nfcCode, dateScan, taskStatus))
                .thenReturn(Completable.error(Throwable()))

        presenter.taskStatus = taskStatus
        Whitebox.invokeMethod<Void>(presenter, methodName, nfcCode, route)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(scanNfcInteractor).updateNfcTime(route.id, nfcCode, dateScan, taskStatus)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).openQuestionsFragment(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(), ArgumentMatchers.any())
    }
}