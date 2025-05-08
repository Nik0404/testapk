package com.acroninspector.app.presentation.fragment.notifications

import com.acroninspector.app.R
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractor
import com.acroninspector.app.presentation.fragment.notifications.readed.ReadedNotificationsPresenter
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
class ReadedNotificationsPresenterTest {

    private lateinit var presenter: ReadedNotificationsPresenter

    @Mock
    lateinit var interactor: NotificationsInteractor

    @Mock
    lateinit var viewState: `NotificationsView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = ReadedNotificationsPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeNotifications(): List<DisplayNotification> {
        return arrayListOf(
                DisplayNotification("", "", "", 1, 1, "", "", 1, "", ""),
                DisplayNotification("", "", "", 2, 2, "", "", 2, "", "")
        )
    }

    @Test
    fun loadNotifications_Success() {
        val methodName = "loadNotifications"
        val notifications = getFakeNotifications()

        `when`(interactor.getReadedNotifications())
                .thenReturn(Flowable.just(notifications))

        Whitebox.invokeMethod<Void>(presenter, methodName, false)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getReadedNotifications()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateNotifications(notifications)
        inOrder.verify(viewState).hideEmptyState()
        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadNotifications_Empty() {
        val methodName = "loadNotifications"

        `when`(interactor.getReadedNotifications())
                .thenReturn(Flowable.just(arrayListOf()))

        Whitebox.invokeMethod<Void>(presenter, methodName, false)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getReadedNotifications()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateNotifications(arrayListOf())
        inOrder.verify(viewState).showEmptyState()
        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadNotifications_Failed() {
        val methodName = "loadNotifications"

        `when`(interactor.getReadedNotifications())
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName, false)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getReadedNotifications()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)
        verify(viewState, never()).updateNotifications(getFakeNotifications())
        verify(viewState, never()).hideEmptyState()
        verify(viewState, never()).showEmptyState()
    }

    @Test
    fun deleteNotification_Success() {
        val position = 1
        val fakeNotifications = getFakeNotifications()

        `when`(interactor.deleteNotification(fakeNotifications[position].taskId))
                .thenReturn(Completable.complete())

        Whitebox.setInternalState(presenter, "notifications", fakeNotifications)

        presenter.onClickDelete(position)

        verify(interactor).deleteNotification(fakeNotifications[position].taskId)
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun deleteNotification_Failed() {
        val position = 1
        val fakeNotifications = getFakeNotifications()

        `when`(interactor.deleteNotification(fakeNotifications[position].taskId))
                .thenReturn(Completable.error(Throwable()))

        Whitebox.setInternalState(presenter, "notifications", fakeNotifications)

        presenter.onClickDelete(position)

        verify(interactor).deleteNotification(fakeNotifications[position].taskId)
        verify(viewState).showSnackbar(R.string.error_message)
    }
}