package com.acroninspector.app.di.fragment.notifications.unreaded

import com.acroninspector.app.di.fragment.notifications.NotificationsModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractor
import com.acroninspector.app.presentation.adapter.notifications.NotificationsAdapter
import com.acroninspector.app.presentation.fragment.notifications.unreaded.NewNotificationsPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [NotificationsModule::class])
class NewNotificationsModule : BaseModule {

    @Provides
    @PerScreen
    fun providePresenter(notificationsInteractor: NotificationsInteractor): NewNotificationsPresenter {
        return NewNotificationsPresenter(notificationsInteractor)
    }

    @Provides
    @PerScreen
    fun provideAdapter(): NotificationsAdapter {
        return NotificationsAdapter()
    }
}