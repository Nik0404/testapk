package com.acroninspector.app.presentation.fragment.tasks.inprogress

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.task.TaskInteractor
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
class InProgressTasksPresenterTest {

    private lateinit var presenter: InProgressTasksPresenter

    @Mock
    lateinit var interactor: TaskInteractor

    @Mock
    lateinit var viewState: `InProgressTasksView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = InProgressTasksPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getStatusCodesForCompletedTasks(): ArrayList<Int> {
        return arrayListOf(
                DatabaseConstants.TASK_STATUS_NEW,
                DatabaseConstants.TASK_STATUS_IN_PROGRESS
        )
    }

    private fun getFakeTasks(): List<DisplayTask> {
        return arrayListOf(
            DisplayTask(
                0, 0, "1", "name 1", 1, "executor 1",
                "1.1", "1.2", 1, 1,
                0, DatabaseConstants.TASK_STATUS_COMPLETED, "", "", ""
            ),
            DisplayTask(
                1, 0, "2", "name 2", 2, "executor 2",
                "2.1", "2.2", 2, 2,
                0, DatabaseConstants.TASK_STATUS_COMPLETED, "", "", ""
            ),
            DisplayTask(
                2, 0, "3", "name 3", 3, "executor 3",
                "3.1", "3.2", 3, 3,
                0, DatabaseConstants.TASK_STATUS_COMPLETED, "", "", ""
            ),
            DisplayTask(
                3, 0, "4", "name 4", 4, "executor 4",
                "4.1", "4.2", 4, 4,
                0, DatabaseConstants.TASK_STATUS_COMPLETED, "", "", ""
            )
        )
    }

    @Test
    fun loadInProgressTasks_Success() {
        val methodName = "loadTasks"
        val statusCodes = getStatusCodesForCompletedTasks()

        Mockito.`when`(interactor.getTasksByStatusCodes(statusCodes))
                .thenReturn(Flowable.just(getFakeTasks()))

        Whitebox.invokeMethod<Void>(presenter, methodName, statusCodes)

        val inOrder = Mockito.inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getTasksByStatusCodes(statusCodes)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateTasks(getFakeTasks())
        inOrder.verify(viewState).hideEmptyState()
        Mockito.verify(viewState, Mockito.never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadInProgressTasks_Empty() {
        val methodName = "loadTasks"
        val statusCodes = getStatusCodesForCompletedTasks()

        Mockito.`when`(interactor.getTasksByStatusCodes(statusCodes))
                .thenReturn(Flowable.just(arrayListOf()))

        Whitebox.invokeMethod<Void>(presenter, methodName, statusCodes)

        val inOrder = Mockito.inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getTasksByStatusCodes(statusCodes)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateTasks(arrayListOf())
        inOrder.verify(viewState).showEmptyState()
        Mockito.verify(viewState, Mockito.never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadInProgressTasks_Failed() {
        val methodName = "loadTasks"
        val statusCodes = getStatusCodesForCompletedTasks()

        Mockito.`when`(interactor.getTasksByStatusCodes(statusCodes))
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName, statusCodes)

        val inOrder = Mockito.inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getTasksByStatusCodes(statusCodes)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEmptyState()
        Mockito.verify(viewState, Mockito.never()).updateTasks(getFakeTasks())
        Mockito.verify(viewState, Mockito.never()).hideEmptyState()
    }

    @Test
    fun onTaskClicked() {
        val position = 0
        val tasks = getFakeTasks()

        Whitebox.setInternalState(presenter, "tasks", tasks)

        presenter.onTaskClicked(position)

        Mockito.verify(viewState).openTaskDetails(tasks[position])
    }
}