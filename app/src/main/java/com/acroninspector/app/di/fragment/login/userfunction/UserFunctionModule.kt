package com.acroninspector.app.di.fragment.login.userfunction

import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.di.fragment.login.BaseLoginModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.userfunction.UserFunctionPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [BaseLoginModule::class])
class UserFunctionModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(
            loginInteractor: LoginInteractor,
            networkErrorsParser: NetworkErrorsParser
    ): UserFunctionPresenter {
        return UserFunctionPresenter(
                loginInteractor,
                networkErrorsParser
        )
    }
}
