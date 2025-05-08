package com.acroninspector.app.di.fragment.controlprocedure

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.controlprocedure.ControlProcedureFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [ControlProcedureModule::class])
interface ControlProcedureComponent : BaseComponent<ControlProcedureFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<ControlProcedureComponent, ControlProcedureModule>
}