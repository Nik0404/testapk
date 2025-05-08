package com.acroninspector.app.di.fragment.edittask

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.edittask.EditTaskFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [EditTaskModule::class])
interface EditTaskComponent : BaseComponent<EditTaskFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<EditTaskComponent, EditTaskModule>
}