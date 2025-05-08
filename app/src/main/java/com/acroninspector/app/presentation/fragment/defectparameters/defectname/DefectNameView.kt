package com.acroninspector.app.presentation.fragment.defectparameters.defectname

import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.presentation.fragment.defectparameters.DefectParametersView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DefectNameView : DefectParametersView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateDefectNames(list: List<DisplayDefect>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun passDefectNameId(id: Int)
}