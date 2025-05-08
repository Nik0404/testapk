package com.acroninspector.app.presentation.mvp

import androidx.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(@StringRes resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(@StringRes resourceId: Int, text: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(message: String)
}
