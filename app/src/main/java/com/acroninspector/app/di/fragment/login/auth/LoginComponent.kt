package com.acroninspector.app.di.fragment.login.auth

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.login.auth.LoginFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [LoginModule::class])
interface LoginComponent : BaseComponent<LoginFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<LoginComponent, LoginModule>
}
