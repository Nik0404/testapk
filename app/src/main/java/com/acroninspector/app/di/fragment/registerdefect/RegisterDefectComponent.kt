package com.acroninspector.app.di.fragment.registerdefect

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.registerdefect.RegisterDefectFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [RegisterDefectModule::class])
interface RegisterDefectComponent : BaseComponent<RegisterDefectFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<RegisterDefectComponent, RegisterDefectModule>
}