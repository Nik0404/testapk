package com.acroninspector.app.di.fragment.taskcomment

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.taskcomment.TaskCommentFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [TaskCommentModule::class])
interface TaskCommentComponent : BaseComponent<TaskCommentFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<TaskCommentComponent, TaskCommentModule>
}