package com.acroninspector.app.di.activity.main

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.activity.main.MainActivity
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [MainModule::class])
interface MainComponent : BaseComponent<MainActivity> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<MainComponent, MainModule>
}
