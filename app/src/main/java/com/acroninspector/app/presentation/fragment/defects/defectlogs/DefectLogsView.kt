package com.acroninspector.app.presentation.fragment.defects.defectlogs

import com.acroninspector.app.presentation.fragment.defects.DefectsBaseView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DefectLogsView : DefectsBaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openSearchFragment()
}