package com.acroninspector.app.presentation.fragment.taskdetails.bypassmanagement

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractor
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.util.*


@RunWith(PowerMockRunner::class)
class TaskDetailsByPassManagementPresenterTest {

    private lateinit var presenter: TaskDetailsByPassManagementPresenter

    @Mock
    lateinit var interactor: TaskDetailsInteractor

    @Mock
    lateinit var viewState: `TaskDetailsByPassManagementView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TaskDetailsByPassManagementPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeRoutes(): ArrayList<DisplayRoute> {
        return arrayListOf(
                DisplayRoute(0, 0, 1, 1, "1", "1", "1",
                        1, 1, 1, 1, 1, 1),
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

        Mockito.`when`(interactor.getRoutesByTaskId(taskId))
                .thenReturn(Flowable.just(routes))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = Mockito.inOrder(viewState, interactor)

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

        Mockito.`when`(interactor.getRoutesByTaskId(taskId))
                .thenReturn(Flowable.error(Throwable()))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = Mockito.inOrder(viewState, interactor)

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

        Mockito.`when`(interactor.getTaskById(taskId))
                .thenReturn(Flowable.just(task))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = Mockito.inOrder(viewState, interactor)

        inOrder.verify(interactor).getTaskById(taskId)
        inOrder.verify(viewState).setTask(task)

        verify(viewState, never()).hideProgress()
        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadTask_Failed() {
        val methodName = "loadTask"
        val taskId = getFakeTaskId()

        Mockito.`when`(interactor.getTaskById(taskId))
                .thenReturn(Flowable.error(Throwable()))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = Mockito.inOrder(viewState, interactor)

        inOrder.verify(interactor).getTaskById(taskId)
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).showSnackbar(R.string.error_message)
        verify(viewState, never()).setTask(ArgumentMatchers.any())
    }

    @Test
    fun onEditTaskClicked_Success() {
        val taskId = 0
        presenter.taskId = taskId

        presenter.onEditTaskClicked()

        verify(viewState).openEditTaskFragment(taskId)
    }

    @Test
    fun onEditTaskClicked_Failed() {
        val taskId = Constants.DEFAULT_INVALID_ID
        presenter.taskId = taskId

        presenter.onEditTaskClicked()

        verify(viewState, never()).openEditTaskFragment(taskId)
    }
}