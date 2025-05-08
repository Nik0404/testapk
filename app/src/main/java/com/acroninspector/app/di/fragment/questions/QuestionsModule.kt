package com.acroninspector.app.di.fragment.questions

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.checklist.CheckListRepositoryModule
import com.acroninspector.app.di.repository.directory.DirectoryRepositoryModule
import com.acroninspector.app.di.repository.equipment.EquipmentRepositoryModule
import com.acroninspector.app.di.repository.localdefect.LocalDefectRepositoryModule
import com.acroninspector.app.di.repository.nfc.NfcRepositoryModule
import com.acroninspector.app.di.repository.route.RouteRepositoryModule
import com.acroninspector.app.domain.interactors.checklist.CheckListInteractor
import com.acroninspector.app.domain.interactors.checklist.CheckListInteractorImpl
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractorImpl
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractorImpl
import com.acroninspector.app.domain.repositories.*
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogByPass
import com.acroninspector.app.presentation.fragment.questions.QuestionsPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [
    CheckListRepositoryModule::class,
    LocalDefectRepositoryModule::class,
    RouteRepositoryModule::class,
    NfcRepositoryModule::class,
    EquipmentRepositoryModule::class,
    DirectoryRepositoryModule::class
])
class QuestionsModule : BaseModule {

    @Provides
    fun providePresenter(
            checkListInteractor: CheckListInteractor,
            scanNfcInteractor: ScanNfcInteractor
    ): QuestionsPresenter {
        return QuestionsPresenter(checkListInteractor, scanNfcInteractor)
    }

    @PerScreen
    @Provides
    fun provideCheckListInteractor(
            checkListRepository: CheckListRepository,
            localDefectRepository: LocalDefectRepository,
            routeRepository: RouteRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): CheckListInteractor {
        return CheckListInteractorImpl(
                checkListRepository,
                localDefectRepository,
                routeRepository,
                preferencesRepository,
                schedulersProvider)
    }

    @PerScreen
    @Provides
    fun provideScanNfcInteractor(
            nfcRepository: NfcRepository,
            routeRepository: RouteRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): ScanNfcInteractor {
        return ScanNfcInteractorImpl(
                nfcRepository,
                routeRepository,
                preferencesRepository,
                schedulersProvider
        )
    }

    @PerScreen
    @Provides
    fun provideEquipmentInteractor(
            equipmentRepository: EquipmentRepository,
            directoryRepository: DirectoryRepository,
            nfcRepository: NfcRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): EquipmentInteractor {
        return EquipmentInteractorImpl(
                directoryRepository,
                equipmentRepository,
                nfcRepository,
                preferencesRepository,
                schedulersProvider)
    }

    @Provides
    fun provideByPassEquipmentDialog(interactor: EquipmentInteractor): EquipmentCardDialogByPass {
        return EquipmentCardDialogByPass(interactor)
    }
}