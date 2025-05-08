package com.acroninspector.app.di.fragment.equipments

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.directory.DirectoryRepositoryModule
import com.acroninspector.app.di.repository.equipment.EquipmentRepositoryModule
import com.acroninspector.app.di.repository.nfc.NfcRepositoryModule
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractorImpl
import com.acroninspector.app.domain.repositories.DirectoryRepository
import com.acroninspector.app.domain.repositories.EquipmentRepository
import com.acroninspector.app.domain.repositories.NfcRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import dagger.Module
import dagger.Provides

@Module(includes = [
    DirectoryRepositoryModule::class,
    EquipmentRepositoryModule::class,
    NfcRepositoryModule::class])
class EquipmentsBaseModule {

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
}