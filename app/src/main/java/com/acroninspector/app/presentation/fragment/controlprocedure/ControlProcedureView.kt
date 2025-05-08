package com.acroninspector.app.presentation.fragment.controlprocedure

import androidx.annotation.StringRes
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ControlProcedureView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateControlProcedures(controlProcedures: List<DisplayControlProcedure>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateControlProcedureItem(position: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeFragment()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideKeyboard()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorDialog(@StringRes message: Int)
}