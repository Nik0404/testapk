package com.acroninspector.app.di.fragment.taskdetails.bypass

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.fragment.taskdetails.TaskDetailsModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.directory.DirectoryRepositoryModule
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractorImpl
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractorImpl
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractor
import com.acroninspector.app.domain.repositories.*
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogByPass
import com.acroninspector.app.presentation.fragment.taskdetails.bypass.TaskDetailsByPassPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [
    TaskDetailsModule::class,
    DirectoryRepositoryModule::class
])
class TaskDetailsByPassModule : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(
            taskDetailsInteractor: TaskDetailsInteractor,
            scanNfcInteractor: ScanNfcInteractor
    ): TaskDetailsByPassPresenter {
        return TaskDetailsByPassPresenter(taskDetailsInteractor, scanNfcInteractor)
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