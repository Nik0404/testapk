package com.acroninspector.app.di.fragment.defectdetails

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.defectdetails.DefectDetailsFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [DefectDetailsModule::class])
interface DefectDetailsComponent : BaseComponent<DefectDetailsFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DefectDetailsComponent, DefectDetailsModule>
}