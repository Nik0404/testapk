package com.acroninspector.app.di.fragment.controlprocedure

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.route.RouteRepositoryModule
import com.acroninspector.app.domain.interactors.controlprocedure.ControlProcedureInteractor
import com.acroninspector.app.domain.interactors.controlprocedure.ControlProcedureInteractorImpl
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.RouteRepository
import com.acroninspector.app.presentation.adapter.controlprocedure.ControlProcedureAdapter
import com.acroninspector.app.presentation.fragment.controlprocedure.ControlProcedurePresenter
import dagger.Module
import dagger.Provides

@Module(includes = [RouteRepositoryModule::class])
class ControlProcedureModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(
            controlProcedureInteractor: ControlProcedureInteractor
    ): ControlProcedurePresenter {
        return ControlProcedurePresenter(controlProcedureInteractor)
    }

    @PerScreen
    @Provides
    fun provideInteractor(
            routeRepository: RouteRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): ControlProcedureInteractor {
        return ControlProcedureInteractorImpl(routeRepository, preferencesRepository, schedulersProvider)
    }

    @PerScreen
    @Provides
    fun provideAdapter(): ControlProcedureAdapter {
        return ControlProcedureAdapter()
    }
}