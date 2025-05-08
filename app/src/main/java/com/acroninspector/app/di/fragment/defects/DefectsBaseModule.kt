package com.acroninspector.app.di.fragment.defects

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.defectlog.DefectLogRepositoryModule
import com.acroninspector.app.di.repository.localdefect.LocalDefectRepositoryModule
import com.acroninspector.app.di.repository.session.SessionRepositoryModule
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractor
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractorImpl
import com.acroninspector.app.domain.repositories.DefectLogRepository
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import dagger.Module
import dagger.Provides

@Module(includes = [
    DefectLogRepositoryModule::class,
    LocalDefectRepositoryModule::class,
    SessionRepositoryModule::class])
class DefectsBaseModule {

    @PerScreen
    @Provides
    fun provideInteractor(
            sessionRepository: SessionRepository,
            defectLogRepository: DefectLogRepository,
            localDefectRepository: LocalDefectRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): DefectLogInteractor {
        return DefectLogInteractorImpl(
                sessionRepository,
                defectLogRepository,
                localDefectRepository,
                preferencesRepository,
                schedulersProvider)
    }
}