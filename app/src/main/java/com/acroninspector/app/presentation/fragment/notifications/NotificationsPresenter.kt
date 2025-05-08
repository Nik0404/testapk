package com.acroninspector.app.presentation.fragment.notifications

import com.acroninspector.app.R
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import io.reactivex.Flowable
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

abstract class NotificationsPresenter<ViewState : NotificationsView>(
        private val notificationsInteractor: NotificationsInteractor
) : BasePresenter<NotificationsView>() {

    private var notifications: List<DisplayNotification> = ArrayList()

    protected fun loadNotifications(isNew: Boolean) {
        viewState.showProgress()
        subscriptions.add(handleLoadingNotifications(isNew)
                .subscribe({
                    viewState.hideProgress()
                    viewState.updateNotifications(it)

                    handleEmptyState(it.isEmpty())
                    notifications = it
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                })
        )
    }

    private fun handleLoadingNotifications(isNew: Boolean): Flowable<List<DisplayNotification>> {
        return if (isNew) {
            notificationsInteractor.getUnreadedNotifications()
        } else notificationsInteractor.getReadedNotifications()
    }

    fun onClickNotification(position: Int) {
        val executorId = notificationsInteractor.getExecutorId()

        viewState.showNotificationDialog(position, notifications[position], executorId)
    }

    fun onGoToTaskClicked(position: Int) {
        val currentTime = DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)
        val notificationDateOfReading = notifications[position].dateOfReading

        if (notificationDateOfReading.isEmpty()) {
            viewState.showProgress()
            subscriptions.add(notificationsInteractor
                    .readNotification(notifications[position].taskId, currentTime)
                    .subscribe({
                        viewState.hideProgress()
                        openTaskDetailsFragment(it)
                    }, {
                        viewState.hideProgress()
                        openTaskDetailsFragment(position)

                        viewState.showToast(R.string.error_message)
                        Timber.e(it)
                    }))
        } else openTaskDetailsFragment(position)
    }

    private fun openTaskDetailsFragment(task: DisplayTask) {
        viewState.openTaskDetailsFragment(task)
    }

    private fun openTaskDetailsFragment(position: Int) {
        viewState.showProgress()
        subscriptions.add(notificationsInteractor
                .getTaskById(notifications[position].taskId)
                .subscribe({
                    viewState.hideProgress()
                    openTaskDetailsFragment(it)
                }, {
                    viewState.hideProgress()
                    viewState.showToast(R.string.error_message)
                    Timber.e(it)
                }))
    }

    fun onClickDelete(position: Int) {
        subscriptions.add(notificationsInteractor
                .deleteNotification(notifications[position].taskId)
                .subscribe({}, {
                    viewState.showSnackbar(R.string.error_message)
                    Timber.e(it)
                })
        )
    }

    private fun handleEmptyState(listIsEmpty: Boolean) {
        if (listIsEmpty)
            viewState.showEmptyState()
        else viewState.hideEmptyState()
    }
}