package com.acroninspector.app.di.fragment.defectcause

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.defectparameters.defectcause.DefectCauseFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [DefectCauseModule::class])
interface DefectCauseComponent : BaseComponent<DefectCauseFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DefectCauseComponent, DefectCauseModule>
}