package com.acroninspector.app.di.fragment.equipments.nested

import com.acroninspector.app.di.global.base.BaseComponent
import com.acroninspector.app.di.global.base.BaseComponentBuilder
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.presentation.fragment.equipments.nested.NestedEquipmentFragment
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [NestedEquipmentModule::class])
interface NestedEquipmentComponent : BaseComponent<NestedEquipmentFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<NestedEquipmentComponent, NestedEquipmentModule>
}
