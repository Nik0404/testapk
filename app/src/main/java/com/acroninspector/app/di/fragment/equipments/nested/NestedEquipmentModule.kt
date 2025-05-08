package com.acroninspector.app.di.fragment.equipments.nested

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.fragment.equipments.EquipmentsBaseModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.nfc.NfcRepositoryModule
import com.acroninspector.app.di.repository.route.RouteRepositoryModule
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractorImpl
import com.acroninspector.app.domain.repositories.NfcRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.RouteRepository
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogByPass
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogNfc
import com.acroninspector.app.presentation.fragment.equipments.nested.NestedEquipmentPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [
    EquipmentsBaseModule::class,
    NfcRepositoryModule::class,
    RouteRepositoryModule::class
])
class NestedEquipmentModule : BaseModule {

    @Provides
    fun providePresenter(
            equipmentInteractor: EquipmentInteractor,
            scanNfcInteractor: ScanNfcInteractor
    ): NestedEquipmentPresenter {
        return NestedEquipmentPresenter(equipmentInteractor, scanNfcInteractor)
    }

    @Provides
    fun provideNfcEquipmentDialog(interactor: EquipmentInteractor): EquipmentCardDialogNfc {
        return EquipmentCardDialogNfc(interactor)
    }

    @Provides
    fun provideByPassEquipmentDialog(interactor: EquipmentInteractor): EquipmentCardDialogByPass {
        return EquipmentCardDialogByPass(interactor)
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

}
