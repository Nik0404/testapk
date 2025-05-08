package com.acroninspector.app.di.fragment.taskcomment

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.task.TaskRepositoryModule
import com.acroninspector.app.domain.interactors.taskcomment.TaskCommentInteractor
import com.acroninspector.app.domain.interactors.taskcomment.TaskCommentInteractorImpl
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import com.acroninspector.app.presentation.fragment.taskcomment.TaskCommentPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [TaskRepositoryModule::class])
class TaskCommentModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(taskCommentInteractor: TaskCommentInteractor): TaskCommentPresenter {
        return TaskCommentPresenter(taskCommentInteractor)
    }

    @PerScreen
    @Provides
    fun provideInteractor(
            taskRepository: TaskRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): TaskCommentInteractor {
        return TaskCommentInteractorImpl(taskRepository, preferencesRepository, schedulersProvider)
    }
}