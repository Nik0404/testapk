package com.acroninspector.app.di.repository.executor

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.ExecutorDao
import com.acroninspector.app.data.repositories.ExecutorRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.ExecutorRepository
import dagger.Module
import dagger.Provides

@Module
class ExecutorRepositoryModule {

    @PerScreen
    @Provides
    fun provideExecutorRepository(executorDao: ExecutorDao): ExecutorRepository {
        return ExecutorRepositoryImpl(executorDao)
    }

    @PerScreen
    @Provides
    fun provideExecutorDao(appDatabase: AppDatabase): ExecutorDao {
        return appDatabase.executorDao()
    }
}