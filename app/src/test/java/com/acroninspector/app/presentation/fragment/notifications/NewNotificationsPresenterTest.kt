package com.acroninspector.app.presentation.fragment.notifications

import com.acroninspector.app.R
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractor
import com.acroninspector.app.presentation.fragment.notifications.unreaded.NewNotificationsPresenter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
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
class NewNotificationsPresenterTest {

    private lateinit var presenter: NewNotificationsPresenter

    @Mock
    lateinit var interactor: NotificationsInteractor

    @Mock
    lateinit var viewState: `NotificationsView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = NewNotificationsPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeNotifications(): List<DisplayNotification> {
        return arrayListOf(
                DisplayNotification("", "", "", 1, 1, "", "", 1, "", ""),
                DisplayNotification("", "123", "", 2, 2, "", "", 2, "", "")
        )
    }

    private fun getFakeTask(): DisplayTask {
        return DisplayTask(
            0, 0, "", "", 1, "",
            "", "", 0, 0, 0,
            0, "", "", ""
        )
    }

    @Test
    fun loadNotifications_Success() {
        val methodName = "loadNotifications"
        val notifications = getFakeNotifications()

        `when`(interactor.getUnreadedNotifications())
                .thenReturn(Flowable.just(notifications))

        Whitebox.invokeMethod<Void>(presenter, methodName, true)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnreadedNotifications()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateNotifications(notifications)
        inOrder.verify(viewState).hideEmptyState()

        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadNotifications_Empty() {
        val methodName = "loadNotifications"

        `when`(interactor.getUnreadedNotifications())
                .thenReturn(Flowable.just(arrayListOf()))

        Whitebox.invokeMethod<Void>(presenter, methodName, true)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnreadedNotifications()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateNotifications(arrayListOf())
        inOrder.verify(viewState).showEmptyState()

        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadNotifications_Failed() {
        val methodName = "loadNotifications"

        `when`(interactor.getUnreadedNotifications())
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName, true)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getUnreadedNotifications()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateNotifications(getFakeNotifications())
        verify(viewState, never()).hideEmptyState()
        verify(viewState, never()).showEmptyState()
    }

    @Test
    fun deleteNotification_Success() {
        val fakeNotifications = getFakeNotifications()
        val position = 1

        `when`(interactor.deleteNotification(fakeNotifications[position].taskId))
                .thenReturn(Completable.complete())

        Whitebox.setInternalState(presenter, "notifications", fakeNotifications)

        presenter.onClickDelete(position)

        verify(interactor).deleteNotification(fakeNotifications[position].taskId)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun deleteNotification_Failed() {
        val fakeNotifications = getFakeNotifications()
        val position = 1

        `when`(interactor.deleteNotification(fakeNotifications[position].taskId))
                .thenReturn(Completable.error(Throwable()))

        Whitebox.setInternalState(presenter, "notifications", fakeNotifications)

        presenter.onClickDelete(position)

        verify(interactor).deleteNotification(fakeNotifications[position].taskId)
        verify(viewState).showSnackbar(R.string.error_message)
    }

    @Test
    fun onClickNotification() {
        val notifications = getFakeNotifications()
        val position = 0
        val executorId = notifications[position].executorId!!

        `when`(interactor.getExecutorId()).thenReturn(executorId)

        Whitebox.setInternalState(presenter, "notifications", notifications)
        presenter.onClickNotification(position)

        verify(viewState).showNotificationDialog(position, notifications[position], executorId)
    }

    @Test
    fun onGoToTaskClicked_NotificationDateReadingNotEmpty() {
        val task = getFakeTask()
        val notifications = getFakeNotifications()
        val position = 1

        `when`(interactor.getTaskById(notifications[position].taskId))
                .thenReturn(Single.just(task))

        Whitebox.setInternalState(presenter, "notifications", notifications)
        presenter.onGoToTaskClicked(position)

        verify(interactor).getTaskById(notifications[position].taskId)

        verify(interactor, never()).readNotification(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun onGoToTaskClicked_Success() {
        val notifications = getFakeNotifications()
        val task = getFakeTask()
        val position = 0

        val currentTime = DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)

        `when`(interactor.readNotification(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
                .thenReturn(Single.just(task))

        Whitebox.setInternalState(presenter, "notifications", notifications)
        presenter.onGoToTaskClicked(position)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).readNotification(notifications[position].taskId, currentTime)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openTaskDetailsFragment(task)

        verify(interactor, never()).getTaskById(ArgumentMatchers.anyInt())
    }

    @Test
    fun onGoToTaskClicked_Failed() {
        val notifications = getFakeNotifications()
        val task = getFakeTask()
        val position = 0

        val currentTime = DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)

        `when`(interactor.readNotification(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
                .thenReturn(Single.error(Throwable()))

        `when`(interactor.getTaskById(notifications[position].taskId))
                .thenReturn(Single.just(task))

        Whitebox.setInternalState(presenter, "notifications", notifications)
        presenter.onGoToTaskClicked(position)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).readNotification(notifications[position].taskId, currentTime)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getTaskById(notifications[position].taskId)
        inOrder.verify(viewState).showToast(R.string.error_message)
    }

    @Test
    fun openTaskDetailsFragment_WithTask() {
        val methodName = "openTaskDetailsFragment"
        val task = getFakeTask()

        Whitebox.invokeMethod<Void>(presenter, methodName, task)

        verify(viewState).openTaskDetailsFragment(task)
    }

    @Test
    fun openTaskDetailsFragment_WithPosition_Success() {
        val methodName = "openTaskDetailsFragment"
        val notifications = getFakeNotifications()
        val task = getFakeTask()
        val position = 0

        `when`(interactor.getTaskById(ArgumentMatchers.anyInt()))
                .thenReturn(Single.just(task))

        Whitebox.setInternalState(presenter, "notifications", notifications)
        Whitebox.invokeMethod<Void>(presenter, methodName, position)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getTaskById(notifications[position].taskId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).openTaskDetailsFragment(task)

        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun openTaskDetailsFragment_WithPosition_Failed() {
        val methodName = "openTaskDetailsFragment"
        val notifications = getFakeNotifications()
        val position = 0

        `when`(interactor.getTaskById(ArgumentMatchers.anyInt()))
                .thenReturn(Single.error(Throwable()))

        Whitebox.setInternalState(presenter, "notifications", notifications)
        Whitebox.invokeMethod<Void>(presenter, methodName, position)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getTaskById(notifications[position].taskId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showToast(R.string.error_message)

        verify(viewState, never()).openTaskDetailsFragment(ArgumentMatchers.any())
    }
}