package com.acroninspector.app.di.fragment.tasks

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.executor.ExecutorRepositoryModule
import com.acroninspector.app.di.repository.task.TaskRepositoryModule
import com.acroninspector.app.domain.interactors.task.TaskInteractor
import com.acroninspector.app.domain.interactors.task.TaskInteractorImpl
import com.acroninspector.app.domain.repositories.ExecutorRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import dagger.Module
import dagger.Provides

@Module(includes = [
    ExecutorRepositoryModule::class,
    TaskRepositoryModule::class])
class TasksModule {

    @PerScreen
    @Provides
    fun provideTaskInteractor(
            taskRepository: TaskRepository,
            executorRepository: ExecutorRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): TaskInteractor {
        return TaskInteractorImpl(
                taskRepository,
                executorRepository,
                preferencesRepository,
                schedulersProvider
        )
    }
}
