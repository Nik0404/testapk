package com.acroninspector.app.di.fragment.defectcause

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.defectcause.DefectCauseRepositoryModule
import com.acroninspector.app.domain.interactors.defectcause.DefectCauseInteractor
import com.acroninspector.app.domain.interactors.defectcause.DefectCauseInteractorImpl
import com.acroninspector.app.domain.repositories.DefectCauseRepository
import com.acroninspector.app.presentation.adapter.defectcauses.DefectCausesAdapter
import com.acroninspector.app.presentation.fragment.defectparameters.defectcause.DefectCausePresenter
import dagger.Module
import dagger.Provides

@Module(includes = [DefectCauseRepositoryModule::class])
class DefectCauseModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(defectCauseInteractor: DefectCauseInteractor): DefectCausePresenter {
        return DefectCausePresenter(defectCauseInteractor)
    }

    @PerScreen
    @Provides
    fun provideInteractor(
            defectCauseRepository: DefectCauseRepository,
            schedulersProvider: SchedulersProvider
    ): DefectCauseInteractor {
        return DefectCauseInteractorImpl(defectCauseRepository, schedulersProvider)
    }

    @PerScreen
    @Provides
    fun provideAdapter(): DefectCausesAdapter {
        return DefectCausesAdapter()
    }

}