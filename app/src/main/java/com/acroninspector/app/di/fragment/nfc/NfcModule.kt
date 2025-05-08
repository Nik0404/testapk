package com.acroninspector.app.di.fragment.nfc

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.nfc.NfcRepositoryModule
import com.acroninspector.app.domain.interactors.nfc.NfcInteractor
import com.acroninspector.app.domain.interactors.nfc.NfcInteractorImpl
import com.acroninspector.app.domain.repositories.NfcRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import dagger.Module
import dagger.Provides

@Module(includes = [NfcRepositoryModule::class])
class NfcModule {

    @PerScreen
    @Provides
    fun provideNfcInteractor(
            nfcRepository: NfcRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): NfcInteractor {
        return NfcInteractorImpl(nfcRepository, preferencesRepository, schedulersProvider)
    }
}