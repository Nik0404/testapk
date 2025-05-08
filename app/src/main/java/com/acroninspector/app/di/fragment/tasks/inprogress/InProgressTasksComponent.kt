package com.acroninspector.app.di.fragment.tasks.inprogress

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.tasks.inprogress.InProgressTasksFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [InProgressTasksModule::class])
interface InProgressTasksComponent : BaseComponent<InProgressTasksFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<InProgressTasksComponent, InProgressTasksModule>
}
