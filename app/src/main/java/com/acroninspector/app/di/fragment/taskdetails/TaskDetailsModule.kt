package com.acroninspector.app.di.fragment.taskdetails

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.checklist.CheckListRepositoryModule
import com.acroninspector.app.di.repository.equipment.EquipmentRepositoryModule
import com.acroninspector.app.di.repository.localdefect.LocalDefectRepositoryModule
import com.acroninspector.app.di.repository.nfc.NfcRepositoryModule
import com.acroninspector.app.di.repository.route.RouteRepositoryModule
import com.acroninspector.app.di.repository.task.TaskRepositoryModule
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractor
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractorImpl
import com.acroninspector.app.domain.repositories.CheckListRepository
import com.acroninspector.app.domain.repositories.EquipmentRepository
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import com.acroninspector.app.domain.repositories.NfcRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.RouteRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import com.acroninspector.app.presentation.adapter.route.RoutesAdapter
import dagger.Module
import dagger.Provides

@Module(includes = [
    TaskRepositoryModule::class,
    EquipmentRepositoryModule::class,
    RouteRepositoryModule::class,
    NfcRepositoryModule::class,
    LocalDefectRepositoryModule::class,
    CheckListRepositoryModule::class])
class TaskDetailsModule {

    @PerScreen
    @Provides
    fun provideRouteInteractor(
            routeRepository: RouteRepository,
            taskRepository: TaskRepository,
            equipmentRepository: EquipmentRepository,
            nfcRepository: NfcRepository,
            preferencesRepository: PreferencesRepository,
            localDefectRepository: LocalDefectRepository,
            schedulersProvider: SchedulersProvider,
            checkListRepository: CheckListRepository,
    ): TaskDetailsInteractor {
        return TaskDetailsInteractorImpl(
                routeRepository,
                taskRepository,
                equipmentRepository,
                nfcRepository,
                preferencesRepository,
                localDefectRepository,
                schedulersProvider,
                checkListRepository)
    }

    @PerScreen
    @Provides
    fun provideRoutesAdapter(): RoutesAdapter {
        return RoutesAdapter()
    }
}