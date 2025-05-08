package com.acroninspector.app.di.fragment.defects.defects

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.defects.defectlogs.DefectLogsFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [DefectsModule::class])
interface DefectsComponent : BaseComponent<DefectLogsFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DefectsComponent, DefectsModule>
}