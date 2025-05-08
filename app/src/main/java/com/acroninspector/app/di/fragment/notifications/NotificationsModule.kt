package com.acroninspector.app.di.fragment.notifications

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.notifications.NotificationsRepositoryModule
import com.acroninspector.app.di.repository.task.TaskRepositoryModule
import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractor
import com.acroninspector.app.domain.interactors.notifications.NotificationsInteractorImpl
import com.acroninspector.app.domain.repositories.NotificationsRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import dagger.Module
import dagger.Provides

@Module(includes = [
    NotificationsRepositoryModule::class,
    TaskRepositoryModule::class])
class NotificationsModule {

    @PerScreen
    @Provides
    fun provideInteractor(
            notificationsRepository: NotificationsRepository,
            taskRepository: TaskRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): NotificationsInteractor {
        return NotificationsInteractorImpl(
                notificationsRepository,
                taskRepository,
                preferencesRepository,
                schedulersProvider)
    }
}
