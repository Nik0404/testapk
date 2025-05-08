package com.acroninspector.app.di.fragment.tasks.completed

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.tasks.completed.CompletedTasksFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [CompletedTasksModule::class])
interface CompletedTasksComponent : BaseComponent<CompletedTasksFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<CompletedTasksComponent, CompletedTasksModule>
}
