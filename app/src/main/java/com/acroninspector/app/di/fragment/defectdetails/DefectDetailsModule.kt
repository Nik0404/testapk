package com.acroninspector.app.di.fragment.defectdetails

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.localdefect.LocalDefectRepositoryModule
import com.acroninspector.app.domain.interactors.defectdetails.DefectDetailsInteractor
import com.acroninspector.app.domain.interactors.defectdetails.DefectDetailsInteractorImpl
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import com.acroninspector.app.presentation.fragment.defectdetails.DefectDetailsPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [LocalDefectRepositoryModule::class])
class DefectDetailsModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(interactor: DefectDetailsInteractor): DefectDetailsPresenter {
        return DefectDetailsPresenter(interactor)
    }

    @PerScreen
    @Provides
    fun provideInteractor(
            localDefectRepository: LocalDefectRepository,
            schedulersProvider: SchedulersProvider
    ): DefectDetailsInteractor {
        return DefectDetailsInteractorImpl(localDefectRepository, schedulersProvider)
    }
}