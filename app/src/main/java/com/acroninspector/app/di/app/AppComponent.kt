package com.acroninspector.app.di.app

import com.acroninspector.app.di.global.ComponentHolder
import com.acroninspector.app.di.global.scope.PerApplication
import dagger.Component

@PerApplication
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectComponentsHolder(holder: ComponentHolder)
}
