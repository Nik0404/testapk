package com.acroninspector.app.di.repository.task

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.TaskDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.repositories.TaskRepositoryImpl
import com.acroninspector.app.data.util.mappers.TaskMapper
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.FunctionsApiModule
import com.acroninspector.app.domain.repositories.TaskRepository
import dagger.Module
import dagger.Provides

@Module(includes = [FunctionsApiModule::class])
class TaskRepositoryModule {

    @PerScreen
    @Provides
    fun provideTaskRepository(
            functionsApi: FunctionsApi,
            taskDao: TaskDao,
            taskMapper: TaskMapper
    ): TaskRepository {
        return TaskRepositoryImpl(functionsApi, taskDao, taskMapper)
    }

    @PerScreen
    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }

    @PerScreen
    @Provides
    fun provideTaskMapper(): TaskMapper {
        return TaskMapper()
    }
}