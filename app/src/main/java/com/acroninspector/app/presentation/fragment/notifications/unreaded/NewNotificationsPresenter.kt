package com.acroninspector.app.presentation.fragment.notifications.unreaded

import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractor
import com.acroninspector.app.presentation.fragment.notifications.NotificationsPresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class NewNotificationsPresenter(
        notificationsInteractor: NotificationsInteractor
) : NotificationsPresenter<NewNotificationsView>(notificationsInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadNotifications(true)
    }
}