package com.acroninspector.app.di.fragment.login.userfunction

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.login.userfunction.UserFunctionFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [UserFunctionModule::class])
interface UserFunctionComponent : BaseComponent<UserFunctionFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<UserFunctionComponent, UserFunctionModule>
}
