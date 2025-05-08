package com.acroninspector.app.di.fragment.tasks.inprogress

import com.acroninspector.app.di.fragment.tasks.TasksModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.task.TaskInteractor
import com.acroninspector.app.presentation.adapter.tasks.TasksAdapter
import com.acroninspector.app.presentation.fragment.tasks.inprogress.InProgressTasksPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [TasksModule::class])
class InProgressTasksModule : BaseModule {

    @Provides
    @PerScreen
    fun providePresenter(taskInteractor: TaskInteractor): InProgressTasksPresenter {
        return InProgressTasksPresenter(taskInteractor)
    }

    @Provides
    @PerScreen
    fun provideAdapter(): TasksAdapter {
        return TasksAdapter()
    }
}
