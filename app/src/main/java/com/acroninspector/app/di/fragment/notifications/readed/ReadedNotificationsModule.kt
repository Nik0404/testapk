package com.acroninspector.app.di.fragment.notifications.readed

import com.acroninspector.app.di.fragment.notifications.NotificationsModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractor
import com.acroninspector.app.presentation.adapter.notifications.NotificationsAdapter
import com.acroninspector.app.presentation.fragment.notifications.readed.ReadedNotificationsPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [NotificationsModule::class])
class ReadedNotificationsModule : BaseModule {

    @Provides
    @PerScreen
    fun providePresenter(notificationsInteractor: NotificationsInteractor): ReadedNotificationsPresenter {
        return ReadedNotificationsPresenter(notificationsInteractor)
    }

    @Provides
    @PerScreen
    fun provideAdapter(): NotificationsAdapter {
        return NotificationsAdapter()
    }
}