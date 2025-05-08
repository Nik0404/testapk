package com.acroninspector.app.di.fragment.defectname

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.defect.DefectRepositoryModule
import com.acroninspector.app.domain.interactors.defectname.DefectNameInteractor
import com.acroninspector.app.domain.interactors.defectname.DefectNameInteractorImpl
import com.acroninspector.app.domain.repositories.DefectRepository
import com.acroninspector.app.presentation.adapter.defectnames.DefectNamesAdapter
import com.acroninspector.app.presentation.fragment.defectparameters.defectname.DefectNamePresenter
import dagger.Module
import dagger.Provides

@Module(includes = [DefectRepositoryModule::class])
class DefectNameModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(defectNameInteractor: DefectNameInteractor): DefectNamePresenter {
        return DefectNamePresenter(defectNameInteractor)
    }

    @PerScreen
    @Provides
    fun provideInteractor(
            defectNameRepository: DefectRepository,
            schedulersProvider: SchedulersProvider
    ): DefectNameInteractor {
        return DefectNameInteractorImpl(defectNameRepository, schedulersProvider)
    }

    @PerScreen
    @Provides
    fun provideAdapter(): DefectNamesAdapter {
        return DefectNamesAdapter()
    }

}