package com.acroninspector.app.di.fragment.equipments.root

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.equipments.root.RootEquipmentFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [RootEquipmentModule::class])
interface RootEquipmentComponent : BaseComponent<RootEquipmentFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<RootEquipmentComponent, RootEquipmentModule>
}
