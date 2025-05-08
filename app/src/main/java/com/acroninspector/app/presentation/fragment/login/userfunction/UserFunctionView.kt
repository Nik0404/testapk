package com.acroninspector.app.presentation.fragment.login.userfunction

import com.acroninspector.app.domain.entity.local.display.DisplayUserFunction
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface UserFunctionView : BaseLoginView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showFunctions(functions: List<DisplayUserFunction>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openLoginFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openHistoryOfAnnotationsFragment()
}
