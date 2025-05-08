package com.acroninspector.app.presentation.fragment.defectparameters.defectcause

import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import com.acroninspector.app.presentation.fragment.defectparameters.DefectParametersView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DefectCauseView : DefectParametersView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateDefectCauses(list: List<DisplayDefectCause>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun passDefectCauseId(id: Int)
}