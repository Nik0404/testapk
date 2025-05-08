package com.acroninspector.app.di.fragment.login

import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.division.DivisionRepositoryModule
import com.acroninspector.app.di.repository.session.SessionRepositoryModule
import com.acroninspector.app.di.repository.user.UserRepositoryModule
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.domain.interactors.login.LoginInteractorImpl
import com.acroninspector.app.domain.repositories.DivisionRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import com.acroninspector.app.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides

@Module(includes = [
    SessionRepositoryModule::class,
    UserRepositoryModule::class,
    DivisionRepositoryModule::class])
class BaseLoginModule {

    @PerScreen
    @Provides
    fun provideLoginInteractor(
            sessionRepository: SessionRepository,
            divisionRepository: DivisionRepository,
            userRepository: UserRepository,
            schedulersProvider: SchedulersProvider,
            preferencesRepository: PreferencesRepository
    ): LoginInteractor {
        return LoginInteractorImpl(
                sessionRepository,
                divisionRepository,
                userRepository,
                schedulersProvider,
                preferencesRepository
        )
    }

    @PerScreen
    @Provides
    fun provideNetworkErrorsParser(): NetworkErrorsParser {
        return NetworkErrorsParser()
    }
}