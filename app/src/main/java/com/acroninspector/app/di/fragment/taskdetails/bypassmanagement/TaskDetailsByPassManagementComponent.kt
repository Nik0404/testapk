package com.acroninspector.app.di.fragment.taskdetails.bypassmanagement

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.taskdetails.bypassmanagement.TaskDetailsByPassManagementFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [TaskDetailsByPassManagementModule::class])
interface TaskDetailsByPassManagementComponent : BaseComponent<TaskDetailsByPassManagementFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<TaskDetailsByPassManagementComponent, TaskDetailsByPassManagementModule>
}