package com.acroninspector.app.di.fragment.login.annotations

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.annotations.HistoryOfAnnotationsFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [HistoryOfAnnotationsModule::class])
interface HistoryOfAnnotationsComponent : BaseComponent<HistoryOfAnnotationsFragment> {

    @Subcomponent.Builder
    interface Builder :
        BaseComponentBuilder<HistoryOfAnnotationsComponent, HistoryOfAnnotationsModule>

}