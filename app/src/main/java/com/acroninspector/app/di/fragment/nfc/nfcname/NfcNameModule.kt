package com.acroninspector.app.di.fragment.nfc.nfcname

import com.acroninspector.app.di.fragment.nfc.NfcModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.nfc.NfcInteractor
import com.acroninspector.app.presentation.fragment.nfc.nfcname.NfcNamePresenter
import dagger.Module
import dagger.Provides

@Module(includes = [NfcModule::class])
class NfcNameModule : BaseModule {

    @PerScreen
    @Provides
    fun provideNfcNamePresenter(nfcInteractor: NfcInteractor): NfcNamePresenter {
        return NfcNamePresenter(nfcInteractor)
    }
}