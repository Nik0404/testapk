package com.acroninspector.app.presentation.fragment.edittask

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.edittask.EditTaskInteractor
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.never
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
class EditTaskPresenterTest {

    private lateinit var presenter: EditTaskPresenter

    @Mock
    lateinit var interactor: EditTaskInteractor

    @Mock
    lateinit var viewState: `EditTaskView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = EditTaskPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeTask(): DisplayTask {
        return DisplayTask(
            0, 0, "1", "name 1", 1, "executor 1",
            "1.1", "1.2", 1, 1,
            0, DatabaseConstants.TASK_STATUS_COMPLETED, "", "", "sad"
        )
    }

    @Test
    fun loadTasks_Success() {
        val methodName = "loadTask"
        val taskId = 1

        Mockito.`when`(interactor.getTaskById(taskId))
                .thenReturn(Flowable.just(getFakeTask()))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getTaskById(taskId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateTask(getFakeTask())

        Mockito.verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadTasks_Failed() {
        val methodName = "loadTask"
        val taskId = 1

        Mockito.`when`(interactor.getTaskById(taskId))
                .thenReturn(Flowable.error(Throwable()))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getTaskById(taskId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        Mockito.verify(viewState, never()).updateTask(any())
    }

    @Test
    fun onChangeControlProcedureClicked_Success() {
        val taskId = 0

        presenter.taskId = taskId
        presenter.onChangeControlProcedureClicked()

        Mockito.verify(viewState).openControlProcedureFragment(taskId)

        Mockito.verify(viewState, never()).showSnackbar(anyInt())
    }

    @Test
    fun onChangeControlProcedureClicked_Failed() {
        val taskId = Constants.DEFAULT_INVALID_ID

        presenter.taskId = taskId
        presenter.onChangeControlProcedureClicked()

        Mockito.verify(viewState).showSnackbar(R.string.error_message)

        Mockito.verify(viewState, never()).openControlProcedureFragment(taskId)
    }

    @Test
    fun onChangeExecutorClicked() {
        val executorGroup = 14
        val executorId = 5

        Whitebox.setInternalState(presenter, "taskExecutorGroup", executorGroup)
        Whitebox.setInternalState(presenter, "taskExecutorId", executorId)

        presenter.onChangeExecutorClicked()

        Mockito.verify(viewState).showExecutorsDialog(executorGroup, executorId)
    }

    @Test
    fun onClickExecutorDialogApply_Success() {
        val taskId = 0
        val executorId = 0

        Mockito.`when`(interactor.updateTaskExecutor(taskId, executorId))
                .thenReturn(Completable.complete())

        presenter.taskId = taskId
        presenter.onClickExecutorDialogApply(executorId)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).updateTaskExecutor(taskId, executorId)
        inOrder.verify(viewState).hideProgress()

        Mockito.verify(viewState, never()).showSnackbar(anyInt())
    }

    @Test
    fun onClickExecutorDialogApply_Failed() {
        val taskId = 0
        val executorId = 0

        Mockito.`when`(interactor.updateTaskExecutor(taskId, executorId))
                .thenReturn(Completable.error(Throwable()))

        presenter.taskId = taskId
        presenter.onClickExecutorDialogApply(executorId)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).updateTaskExecutor(taskId, executorId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)
    }
}