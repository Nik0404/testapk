package com.acroninspector.app.presentation.fragment.notifications.readed

import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractor
import com.acroninspector.app.presentation.fragment.notifications.NotificationsPresenter
import com.acroninspector.app.presentation.fragment.notifications.unreaded.NewNotificationsView
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class ReadedNotificationsPresenter(
        notificationsInteractor: NotificationsInteractor
) : NotificationsPresenter<ReadedNotificationsView>(notificationsInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadNotifications(false)
    }
}