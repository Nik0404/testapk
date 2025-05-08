package com.acroninspector.app.di.repository.directory

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.DirectoryDao
import com.acroninspector.app.data.repositories.DirectoryRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.DirectoryRepository
import dagger.Module
import dagger.Provides

@Module
class DirectoryRepositoryModule {

    @PerScreen
    @Provides
    fun provideDirectoryRepository(dao: DirectoryDao): DirectoryRepository {
        return DirectoryRepositoryImpl(dao)
    }

    @PerScreen
    @Provides
    fun provideDirectoryDao(appDatabase: AppDatabase): DirectoryDao {
        return appDatabase.directoryDao()
    }
}