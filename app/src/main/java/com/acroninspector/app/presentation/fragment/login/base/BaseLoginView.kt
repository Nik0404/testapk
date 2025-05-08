package com.acroninspector.app.presentation.fragment.login.base

import androidx.annotation.StringRes
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseLoginView: BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setAppVersionName(appVersion: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openSupervisedUnitFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openMainScreen()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openWebsite(url: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(@StringRes resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(errorMessage: String, @StringRes actionResId: Int)
}