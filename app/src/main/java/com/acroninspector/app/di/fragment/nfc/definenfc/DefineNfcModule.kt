package com.acroninspector.app.di.fragment.nfc.definenfc

import com.acroninspector.app.di.fragment.nfc.NfcModule
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.interactors.nfc.NfcInteractor
import com.acroninspector.app.presentation.fragment.nfc.definenfc.DefineNfcPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [NfcModule::class])
class DefineNfcModule : BaseModule {

    @PerScreen
    @Provides
    fun provideDefineNfcPresenter(nfcInteractor: NfcInteractor): DefineNfcPresenter {
        return DefineNfcPresenter(nfcInteractor)
    }
}