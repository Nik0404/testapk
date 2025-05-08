package com.acroninspector.app.di.fragment.nfc.nfcname

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.nfc.nfcname.NfcNameFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [NfcNameModule::class])
interface NfcNameComponent : BaseComponent<NfcNameFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<NfcNameComponent, NfcNameModule>
}