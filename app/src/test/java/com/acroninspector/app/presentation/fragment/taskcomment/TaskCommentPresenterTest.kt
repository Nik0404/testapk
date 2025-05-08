package com.acroninspector.app.presentation.fragment.taskcomment

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.taskcomment.TaskCommentInteractor
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
class TaskCommentPresenterTest {

    private lateinit var presenter: TaskCommentPresenter

    @Mock
    lateinit var interactor: TaskCommentInteractor

    @Mock
    lateinit var viewState: `TaskCommentView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TaskCommentPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeTask(): DisplayTask {
        return DisplayTask(
            0, 0, "0", "", 1, "", "",
            "", 0, 0, 0, 0, "", "", ""
        )
    }

    @Test
    fun loadTaskInvalidTaskId() {
        val methodName = "loadTask"

        presenter.taskId = DEFAULT_INVALID_ID
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(interactor, never()).getTaskById(ArgumentMatchers.anyInt())
        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadTask_Success() {
        val methodName = "loadTask"
        val taskId = 1
        val task = getFakeTask()

        `when`(interactor.getTaskById(taskId))
                .thenReturn(Flowable.just(task))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getTaskById(taskId)
        inOrder.verify(viewState).setTask(task)

        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadTask_Failed() {
        val methodName = "loadTask"
        val taskId = 1

        `when`(interactor.getTaskById(taskId))
                .thenReturn(Flowable.error(Throwable()))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(interactor).getTaskById(taskId)

        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).setTask(ArgumentMatchers.any())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun applyClicked_Success() {
        val taskId = 1
        val comment = "comment"

        `when`(interactor.saveTaskComment(taskId, comment))
                .thenReturn(Completable.complete())

        presenter.taskId = taskId
        presenter.onApplyClicked(comment)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).saveTaskComment(taskId, comment)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).closeFragment()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun applyClicked_IncorrectLength() {
        val comment = "commentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcommentcomment"

        presenter.onApplyClicked(comment)

        verify(viewState).showSnackbar(R.string.length_too_long)

        verify(interactor, never()).saveTaskComment(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
        verify(viewState, never()).closeFragment()
        verify(viewState, never()).showProgress()
        verify(viewState, never()).hideProgress()
        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun applyClicked_EmptyComment() {
        val taskId = 1
        val comment = ""

        `when`(interactor.saveTaskComment(taskId, comment))
                .thenReturn(Completable.complete())

        presenter.taskId = taskId
        presenter.onApplyClicked(comment)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).saveTaskComment(taskId, comment)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).closeFragment()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun applyClicked_SpaceComment() {
        val taskId = 1
        val comment = "        "

        `when`(interactor.saveTaskComment(taskId, ""))
                .thenReturn(Completable.complete())

        presenter.taskId = taskId
        presenter.onApplyClicked(comment)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).saveTaskComment(taskId, "")
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).closeFragment()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun applyClicked_Failed() {
        val taskId = 1
        val comment = "comment"

        `when`(interactor.saveTaskComment(taskId, comment))
                .thenReturn(Completable.error(Throwable()))

        presenter.taskId = taskId
        presenter.onApplyClicked(comment)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).saveTaskComment(taskId, comment)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showToast(R.string.error_message)
        inOrder.verify(viewState).closeFragment()

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }
}