package com.acroninspector.app.di.fragment.taskdetails.bypassmanagement

import com.acroninspector.app.di.fragment.taskdetails.TaskDetailsModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractor
import com.acroninspector.app.presentation.fragment.taskdetails.bypassmanagement.TaskDetailsByPassManagementPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [TaskDetailsModule::class])
class TaskDetailsByPassManagementModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(interactor: TaskDetailsInteractor): TaskDetailsByPassManagementPresenter {
        return TaskDetailsByPassManagementPresenter(interactor)
    }
}