package com.acroninspector.app.di.fragment.login.auth

import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.di.fragment.login.BaseLoginModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.auth.LoginPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [BaseLoginModule::class])
class LoginModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(
            loginInteractor: LoginInteractor,
            networkErrorsParser: NetworkErrorsParser
    ): LoginPresenter {
        return LoginPresenter(
                loginInteractor,
                networkErrorsParser
        )
    }
}
