package com.acroninspector.app.di.fragment.defectname

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.defectparameters.defectname.DefectNameFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [DefectNameModule::class])
interface DefectNameComponent : BaseComponent<DefectNameFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DefectNameComponent, DefectNameModule>
}