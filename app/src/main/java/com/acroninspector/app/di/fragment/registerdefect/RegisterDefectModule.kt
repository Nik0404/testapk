package com.acroninspector.app.di.fragment.registerdefect

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.checklist.CheckListRepositoryModule
import com.acroninspector.app.di.repository.defect.DefectRepositoryModule
import com.acroninspector.app.di.repository.defectcause.DefectCauseRepositoryModule
import com.acroninspector.app.di.repository.equipment.EquipmentRepositoryModule
import com.acroninspector.app.di.repository.localdefect.LocalDefectRepositoryModule
import com.acroninspector.app.di.repository.mediafile.MediaFileRepositoryModule
import com.acroninspector.app.domain.interactors.registerdefect.RegisterDefectInteractor
import com.acroninspector.app.domain.interactors.registerdefect.RegisterDefectInteractorImpl
import com.acroninspector.app.domain.repositories.*
import com.acroninspector.app.presentation.fragment.registerdefect.RegisterDefectPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [
    CheckListRepositoryModule::class,
    DefectRepositoryModule::class,
    DefectCauseRepositoryModule::class,
    EquipmentRepositoryModule::class,
    MediaFileRepositoryModule::class,
    LocalDefectRepositoryModule::class])
class RegisterDefectModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(interactor: RegisterDefectInteractor): RegisterDefectPresenter {
        return RegisterDefectPresenter(interactor)
    }

    @PerScreen
    @Provides
    fun provideInteractor(
            localDefectRepository: LocalDefectRepository,
            checkListRepository: CheckListRepository,
            defectCauseRepository: DefectCauseRepository,
            defectRepository: DefectRepository,
            equipmentRepository: EquipmentRepository,
            mediaFileRepository: MediaFileRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): RegisterDefectInteractor {
        return RegisterDefectInteractorImpl(
                localDefectRepository,
                checkListRepository,
                defectCauseRepository,
                defectRepository,
                equipmentRepository,
                mediaFileRepository,
                preferencesRepository,
                schedulersProvider)
    }
}