package com.acroninspector.app.presentation.fragment.tasks.completed

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.task.TaskInteractor
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
class CompletedTasksPresenterTest {

    private lateinit var presenter: CompletedTasksPresenter

    @Mock
    lateinit var interactor: TaskInteractor

    @Mock
    lateinit var viewState: `CompletedTasksView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = CompletedTasksPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getStatusCodesForCompletedTasks(): ArrayList<Int> {
        return arrayListOf(DatabaseConstants.TASK_STATUS_COMPLETED)
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
    fun loadCompletedTasks_Success() {
        val methodName = "loadCompletedTasks"

        `when`(interactor.getCompletedTasks())
                .thenReturn(Flowable.just(getFakeTasks()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getCompletedTasks()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateTasks(getFakeTasks())
        inOrder.verify(viewState).hideEmptyState()
        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadCompletedTasks_Empty() {
        val methodName = "loadCompletedTasks"

        `when`(interactor.getCompletedTasks())
                .thenReturn(Flowable.just(arrayListOf()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getCompletedTasks()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateTasks(arrayListOf())
        inOrder.verify(viewState).showEmptyState()
        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadCompletedTasks_Failed() {
        val methodName = "loadCompletedTasks"

        `when`(interactor.getCompletedTasks())
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getCompletedTasks()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEmptyState()
        verify(viewState, never()).updateTasks(getFakeTasks())
        verify(viewState, never()).hideEmptyState()
    }

    @Test
    fun onTaskClicked() {
        val position = 0
        val tasks = getFakeTasks()

        Whitebox.setInternalState(presenter, "tasks", tasks)

        presenter.onTaskClicked(position)

        verify(viewState).openTaskDetails(tasks[position])
    }
}