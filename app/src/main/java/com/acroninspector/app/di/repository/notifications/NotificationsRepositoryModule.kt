package com.acroninspector.app.di.repository.notifications

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.NotificationsDao
import com.acroninspector.app.data.repositories.NotificationsRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.NotificationsRepository
import dagger.Module
import dagger.Provides

@Module
class NotificationsRepositoryModule {

    @PerScreen
    @Provides
    fun provideNotificationsRepository(notificationsDao: NotificationsDao): NotificationsRepository {
        return NotificationsRepositoryImpl(notificationsDao)
    }

    @PerScreen
    @Provides
    fun provideNotificationsDao(appDatabase: AppDatabase): NotificationsDao {
        return appDatabase.notificationsDao()
    }
}