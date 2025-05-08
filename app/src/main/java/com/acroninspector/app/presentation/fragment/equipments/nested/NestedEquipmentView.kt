package com.acroninspector.app.presentation.fragment.equipments.nested

import com.acroninspector.app.presentation.fragment.equipments.EquipmentBaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface NestedEquipmentView : EquipmentBaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setToolbarTitle(directoryName: String)
}
