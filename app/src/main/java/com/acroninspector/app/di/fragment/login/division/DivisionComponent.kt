package com.acroninspector.app.di.fragment.login.division

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.login.supervisedunit.SupervisedUnitFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [DivisionModule::class])
interface DivisionComponent : BaseComponent<SupervisedUnitFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DivisionComponent, DivisionModule>
}
