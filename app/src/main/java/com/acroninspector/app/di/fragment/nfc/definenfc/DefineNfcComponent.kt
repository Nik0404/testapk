package com.acroninspector.app.di.fragment.nfc.definenfc

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.nfc.definenfc.DefineNfcFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [DefineNfcModule::class])
interface DefineNfcComponent : BaseComponent<DefineNfcFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DefineNfcComponent, DefineNfcModule>
}