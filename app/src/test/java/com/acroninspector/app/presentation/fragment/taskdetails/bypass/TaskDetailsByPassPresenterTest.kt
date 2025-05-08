package com.acroninspector.app.presentation.fragment.taskdetails.bypass

import android.os.BaseBundle
import android.os.Bundle
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_STATUS
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_IN_PROGRESS
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_NEW
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractor
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.util.Calendar

@RunWith(PowerMockRunner::class)
@PrepareForTest(TaskDetailsByPassPresenter::class, Bundle::class)
class TaskDetailsByPassPresenterTest {

    private lateinit var presenter: TaskDetailsByPassPresenter

    @Mock
    lateinit var interactor: TaskDetailsInteractor

    @Mock
    lateinit var scanNfcInteractor: ScanNfcInteractor

    @Mock
    lateinit var viewState: `TaskDetailsByPassView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TaskDetailsByPassPresenter(interactor, scanNfcInteractor)
        presenter.setViewState(viewState)
    }

    private fun getFakeRoutes(): ArrayList<DisplayRoute> {
        return arrayListOf(
                DisplayRoute(0, 0, 1, 1, "1", "1", "1",
                        1, 1, 1, 1, 1, 0),
                DisplayRoute(1, 1, 2, 2, "2", "2", "2",
                        2, 2, 2, 2, 2, 1),
                DisplayRoute(2, 2, 3, 3, "3", "3", "3",
                        3, 3, 3, 3, 3, 1)
        )
    }

    private fun getFakeTask(): DisplayTask {
        return DisplayTask(
            0, 0, "1", "1", 1, "1", "1",
            "1", 1, 1, 1, 1, "", "", ""
        )
    }

    private fun getFakeTaskId(): Int {
        return 1
    }

    @Test
    fun loadRoutes_Success() {
        val methodName = "loadRoutes"

        val routes = getFakeRoutes()
        val taskId = getFakeTaskId()

        `when`(interactor.getRoutesByTaskId(taskId))
                .thenReturn(Flowable.just(routes))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getRoutesByTaskId(taskId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateRoutes(routes)

        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadRoutes_Failed() {
        val methodName = "loadRoutes"
        val taskId = getFakeTaskId()

        `when`(interactor.getRoutesByTaskId(taskId))
                .thenReturn(Flowable.error(Throwable()))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getRoutesByTaskId(taskId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateRoutes(ArgumentMatchers.anyList())
    }

    @Test
    fun loadTask_Success() {
        val methodName = "loadTask"
        val task = getFakeTask()
        val taskId = getFakeTaskId()

        `when`(interactor.getTaskById(taskId))
                .thenReturn(Flowable.just(task))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getTaskById(taskId)
        inOrder.verify(viewState).setTask(task)

        verify(viewState, never()).hideProgress()
        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadTask_Failed() {
        val methodName = "loadTask"
        val taskId = getFakeTaskId()

        `when`(interactor.getTaskById(taskId))
                .thenReturn(Flowable.error(Throwable()))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getTaskById(taskId)

        verify(viewState, never()).hideProgress()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).setTask(ArgumentMatchers.any())
    }

    @Test
    fun routeButtonClicked_WithNewTask() {
        presenter.taskStatus = TASK_STATUS_NEW

        presenter.routeButtonClicked()

        verify(viewState).showStartRouteDialog()
        verify(viewState, never()).showProgress()
    }

    @Test
    fun routeButtonClicked_WithInProgressTask() {
        `when`(interactor.getUnansweredLists(ArgumentMatchers.anyList()))
                .thenReturn(Single.just("1"))

        Whitebox.setInternalState(presenter, "routes", getFakeRoutes())
        presenter.taskStatus = TASK_STATUS_IN_PROGRESS
        presenter.routeButtonClicked()

        verify(viewState).showProgress()
        verify(viewState, never()).showStartRouteDialog()
    }

    @Test
    fun handleFinishRoute_Success_HasUnansweredLists() {
        val methodName = "handleFinishRoute"

        `when`(interactor.getUnansweredLists(ArgumentMatchers.anyList()))
                .thenReturn(Single.just("1"))

        Whitebox.setInternalState(presenter, "routes", getFakeRoutes())
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(interactor, viewState)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnansweredLists(getFakeRoutes())
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showUnfinishedTaskDialog(Constants.UNANSWERED_LISTS, "1")
    }

    @Test
    fun handleFinishRoute_Success_HasNotUnansweredLists() {
        val methodName = "handleFinishRoute"

        val taskId = 1
        val taskStatus = DatabaseConstants.TASK_STATUS_COMPLETED

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val actualDateString = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(interactor.finishRoute(taskId, taskStatus, actualDateString))
                .thenReturn(Completable.complete())

        `when`(interactor.getUnansweredLists(ArgumentMatchers.anyList()))
                .thenReturn(Single.just(""))

        `when`(interactor.getUnansweredNfcMarks(ArgumentMatchers.anyList()))
                .thenReturn(Single.just(""))

        `when`(interactor.finishRoute(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
                .thenReturn(Completable.complete())

        presenter.taskId = taskId
        Whitebox.setInternalState(presenter, "routes", getFakeRoutes())
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(interactor, viewState)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnansweredLists(getFakeRoutes())
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showProgress()

        verify(viewState, never()).showUnfinishedTaskDialog(
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())
    }

    @Test
    fun handleFinishRoute_Failed() {
        val methodName = "handleFinishRoute"

        `when`(interactor.getUnansweredLists(ArgumentMatchers.anyList()))
                .thenReturn(Single.error(Throwable()))

        Whitebox.setInternalState(presenter, "routes", getFakeRoutes())
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(interactor, viewState)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnansweredLists(getFakeRoutes())
        inOrder.verify(viewState).hideProgress()

        verify(viewState, never()).showUnfinishedTaskDialog(
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())
    }

    @Test
    fun onRouteClicked_Success() {
        val position = 0
        val taskId = 0
        val taskStatus = TASK_STATUS_IN_PROGRESS
        val routes = getFakeRoutes()

        val bundle = PowerMockito.mock(Bundle::class.java)
        doNothing().`when`(bundle as BaseBundle).apply {
            putInt("", 0)
        }
        PowerMockito.whenNew(Bundle::class.java)
                .withAnyArguments().thenReturn(bundle)

        Whitebox.setInternalState(presenter, "routes", routes)

        presenter.taskId = taskId
        presenter.taskStatus = taskStatus
//        presenter.onRouteClicked(position)

        verify(viewState).openQuestionsFragment(bundle)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun onRouteClicked_Failed_TaskStatusNew() {
        val position = 0
        val taskStatus = TASK_STATUS_NEW

        presenter.taskStatus = taskStatus
//        presenter.onRouteClicked(position)

        verify(viewState).showSnackbar(R.string.task_new_route_clicked_error_message)
        verify(viewState, never()).openQuestionsFragment(ArgumentMatchers.any())
    }

    @Test
    fun onRouteClicked_Failed_IncorrectTaskStatus() {
        val position = 0
        val taskStatus = DEFAULT_INVALID_STATUS

        presenter.taskStatus = taskStatus
//        presenter.onRouteClicked(position)

        verify(viewState).showSnackbar(R.string.task_new_route_clicked_error_message)
        verify(viewState, never()).openQuestionsFragment(ArgumentMatchers.any())
    }

    @Test
    fun onRouteAttachmentsClicked() {
        val position = 0
        val routes = getFakeRoutes()

        val bundle = PowerMockito.mock(Bundle::class.java)
        doNothing().`when`(bundle as BaseBundle).apply {
            putInt("", 0)
        }
        PowerMockito.whenNew(Bundle::class.java)
                .withAnyArguments().thenReturn(bundle)

        Whitebox.setInternalState(presenter, "routes", routes)

        presenter.onRouteAttachmentsClicked(position)

        verify(viewState).openMediaFilesFragment(bundle)
    }

    @Test
    fun onStartRouteClicked_Success() {
        val taskId = 1
        val taskStatus = TASK_STATUS_IN_PROGRESS

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val actualStartDate = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(interactor.startRoute(taskId, taskStatus, actualStartDate))
                .thenReturn(Completable.complete())

        presenter.taskId = taskId
        presenter.onStartRouteClicked()

        val inOrder = inOrder(interactor, viewState)

        inOrder.verify(interactor).startRoute(taskId, taskStatus, actualStartDate)

        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun onStartRouteClicked_Failed() {
        val taskId = 1
        val taskStatus = TASK_STATUS_IN_PROGRESS

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val actualDateString = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(interactor.startRoute(taskId, taskStatus, actualDateString))
                .thenReturn(Completable.error(Throwable()))

        presenter.taskId = taskId
        presenter.onStartRouteClicked()

        val inOrder = inOrder(interactor, viewState)

        inOrder.verify(interactor).startRoute(taskId, taskStatus, actualDateString)
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
    }

    @Test
    fun onFinishRouteClicked_Success() {
        val taskId = 1
        val taskStatus = DatabaseConstants.TASK_STATUS_COMPLETED

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val actualDateString = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(interactor.finishRoute(taskId, taskStatus, actualDateString))
                .thenReturn(Completable.complete())

        presenter.taskId = taskId
        presenter.onFinishRouteClicked()

        val inOrder = inOrder(interactor, viewState)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).finishRoute(taskId, taskStatus, actualDateString)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun onFinishRouteClicked_Failed() {
        val taskId = 1
        val taskStatus = DatabaseConstants.TASK_STATUS_COMPLETED

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val actualDateString = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(interactor.finishRoute(taskId, taskStatus, actualDateString))
                .thenReturn(Completable.error(Throwable()))

        presenter.taskId = taskId
        presenter.onFinishRouteClicked()

        val inOrder = inOrder(interactor, viewState)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).finishRoute(taskId, taskStatus, actualDateString)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)
    }

    @Test
    fun onTaskCommentClicked_IncorrectTaskId() {
        presenter.taskId = DEFAULT_INVALID_ID
        presenter.taskStatus = 10

        presenter.onTaskCommentClicked(getFakeTask())

        verify(viewState, never()).openTaskCommentFragment(Bundle())
    }

    @Test
    fun onTaskCommentClicked_IncorrectTaskStatus() {
        presenter.taskId = 1
        presenter.taskStatus = DEFAULT_INVALID_STATUS

        presenter.onTaskCommentClicked(getFakeTask())

        verify(viewState, never()).openTaskCommentFragment(Bundle())
    }

    @Test
    fun onTaskCommentClicked_IncorrectTaskBoth() {
        presenter.taskId = DEFAULT_INVALID_ID
        presenter.taskStatus = DEFAULT_INVALID_STATUS

        presenter.onTaskCommentClicked(getFakeTask())

        verify(viewState, never()).openTaskCommentFragment(Bundle())
    }

    @Test
    fun onTaskCommentClicked_Success() {
        val taskId = 1
        val taskStatus = 10

        presenter.taskId = taskId
        presenter.taskStatus = taskStatus

        val bundle = PowerMockito.mock(Bundle::class.java)
        doNothing().`when`(bundle as BaseBundle).apply {
            putInt("", 0)
        }
        PowerMockito.whenNew(Bundle::class.java)
                .withAnyArguments().thenReturn(bundle)

        presenter.onTaskCommentClicked(getFakeTask())

        verify(viewState).openTaskCommentFragment(bundle)
    }

    @Test
    fun onTaskAttachmentsClicked() {
        val bundle = PowerMockito.mock(Bundle::class.java)
        doNothing().`when`(bundle as BaseBundle).apply {
            putInt("", 0)
        }
        PowerMockito.whenNew(Bundle::class.java)
                .withAnyArguments().thenReturn(bundle)

        presenter.onTaskAttachmentsClicked()

        verify(viewState).openMediaFilesFragment(bundle)
    }

    @Test
    fun getRoutePositionById() {
        val methodName = "getRoutePositionById"
        val routes = getFakeRoutes()
        val correctRouteIds = arrayListOf(1, 10, 20)
        val incorrectRouteIds = arrayListOf(10, 20, 30)

        Whitebox.setInternalState(presenter, "routes", routes)

        val correctRoutePosition =
                Whitebox.invokeMethod<Int>(presenter, methodName, correctRouteIds)

        val incorrectRoutePosition =
                Whitebox.invokeMethod<Int>(presenter, methodName, incorrectRouteIds)

        assertEquals(correctRoutePosition, 1)
        assertEquals(incorrectRoutePosition, -1)
    }

    @Test
    fun updateNfcRouteTime_Success() {
        val methodName = "updateNfcRouteTime"

        val taskStatus = TASK_STATUS_IN_PROGRESS
        val nfcCode = "AA:AA:AA:AA:AA"
        val routes = getFakeRoutes()
        val routePosition = 1
        val routeId = routes[routePosition].id

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val dateScan = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(scanNfcInteractor.updateNfcTime(routeId, nfcCode, dateScan, taskStatus))
                .thenReturn(Completable.complete())

        presenter.taskStatus = taskStatus
        Whitebox.setInternalState(presenter, "routes", routes)
        Whitebox.invokeMethod<Void>(presenter, methodName, nfcCode, routePosition)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(scanNfcInteractor).updateNfcTime(routeId, nfcCode, dateScan, taskStatus)
        inOrder.verify(viewState).hideProgress()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun updateNfcRouteTime_Failed() {
        val methodName = "updateNfcRouteTime"

        val taskStatus = TASK_STATUS_IN_PROGRESS
        val nfcCode = "AA:AA:AA:AA:AA"
        val routes = getFakeRoutes()
        val routePosition = 1
        val routeId = routes[routePosition].id

        val currentTimeInMills = Calendar.getInstance().timeInMillis
        val dateScan = DateUtil.convertLongDateToString(currentTimeInMills)

        `when`(scanNfcInteractor.updateNfcTime(routeId, nfcCode, dateScan, taskStatus))
                .thenReturn(Completable.error(Throwable()))

        presenter.taskStatus = taskStatus
        Whitebox.setInternalState(presenter, "routes", routes)
        Whitebox.invokeMethod<Void>(presenter, methodName, nfcCode, routePosition)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(scanNfcInteractor).updateNfcTime(routeId, nfcCode, dateScan, taskStatus)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)
    }

    @Test
    fun onNfcScanned_Success_CorrectPosition() {
        val routes = getFakeRoutes()
        Whitebox.setInternalState(presenter, "routes", routes)

        val nfcCode = "AA:AA:AA:AA:AA"
        val positions = arrayListOf(1, 20, 30)

        `when`(scanNfcInteractor.getRouteIdByNfcCode(nfcCode))
                .thenReturn(Maybe.just(positions))

        `when`(scanNfcInteractor.updateNfcTime(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt())).thenReturn(Completable.complete())

//        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getRouteIdByNfcCode(nfcCode)
        inOrder.verify(scanNfcInteractor).updateNfcTime(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())

        verify(viewState, never()).showErrorDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())
    }

    @Test
    fun onNfcScanned_Success_IncorrectPosition() {
        val routes = getFakeRoutes()
        Whitebox.setInternalState(presenter, "routes", routes)

        val nfcCode = "AA:AA:AA:AA:AA"
        val positions = arrayListOf(-1)

        `when`(scanNfcInteractor.getRouteIdByNfcCode(nfcCode))
                .thenReturn(Maybe.just(positions))
        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.empty())

//        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getRouteIdByNfcCode(nfcCode)
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(scanNfcInteractor, never()).updateNfcTime(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
    }

    @Test
    fun onNfcScanned_Failed() {
        val nfcCode = "AA:AA:AA:AA:AA"

        `when`(scanNfcInteractor.getRouteIdByNfcCode(nfcCode))
                .thenReturn(Maybe.error(Throwable()))

//        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getRouteIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).showErrorDialog(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())
        verify(scanNfcInteractor, never()).updateNfcTime(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())

    }

    @Test
    fun onNfcScanned_Empty() {
        val nfcCode = "AA:AA:AA:AA:AA"

        `when`(scanNfcInteractor.getRouteIdByNfcCode(nfcCode))
                .thenReturn(Maybe.empty())
        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.empty())

//        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getRouteIdByNfcCode(nfcCode)
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(scanNfcInteractor, never()).updateNfcTime(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
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
    fun onRouteDefectsClicked() {
        val routes = getFakeRoutes()
        Whitebox.setInternalState(presenter, "routes", routes)

        val bundle = PowerMockito.mock(Bundle::class.java)
        doNothing().`when`(bundle as BaseBundle).apply {
            putInt("", 0)
        }
        PowerMockito.whenNew(Bundle::class.java)
                .withAnyArguments().thenReturn(bundle)

        presenter.onRouteDefectsClicked(0)

        verify(viewState).openDefectsFragment(bundle)

    }

    @Test
    fun checkAnsweredNfcMarks_Success_WithEmptyMarks() {
        val routes = getFakeRoutes()
        Whitebox.setInternalState(presenter, "routes", routes)

        `when`(interactor.getUnansweredNfcMarks(routes))
                .thenReturn(Single.just(""))

        `when`(interactor.finishRoute(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())).thenReturn(Completable.complete())

        presenter.checkAnsweredNfcMarks()

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnansweredNfcMarks(routes)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(interactor).finishRoute(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())

        verify(viewState, never()).showUnfinishedTaskDialog(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())
    }

    @Test
    fun checkAnsweredNfcMarks_Success() {
        val routes = getFakeRoutes()
        Whitebox.setInternalState(presenter, "routes", routes)

        val unscannedNfcMarks = "Test"
        `when`(interactor.getUnansweredNfcMarks(routes))
                .thenReturn(Single.just(unscannedNfcMarks))

        presenter.checkAnsweredNfcMarks()

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnansweredNfcMarks(routes)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showUnfinishedTaskDialog(Constants.UNSCANNED_NFC_MARKS, unscannedNfcMarks)

        verify(interactor, never()).finishRoute(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())
    }

    @Test
    fun checkAnsweredNfcMarks_Failed() {
        val routes = getFakeRoutes()
        Whitebox.setInternalState(presenter, "routes", routes)

        `when`(interactor.getUnansweredNfcMarks(routes))
                .thenReturn(Single.error(Throwable()))

        presenter.checkAnsweredNfcMarks()

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnansweredNfcMarks(routes)
        inOrder.verify(viewState).hideProgress()

        verify(viewState, never()).showUnfinishedTaskDialog(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())
        verify(interactor, never()).finishRoute(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString())
    }
}