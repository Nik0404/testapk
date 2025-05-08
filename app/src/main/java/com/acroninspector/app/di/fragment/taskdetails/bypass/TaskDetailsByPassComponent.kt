package com.acroninspector.app.di.fragment.taskdetails.bypass

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.taskdetails.bypass.TaskDetailsByPassFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [TaskDetailsByPassModule::class])
interface TaskDetailsByPassComponent : BaseComponent<TaskDetailsByPassFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<TaskDetailsByPassComponent, TaskDetailsByPassModule>
}