package com.acroninspector.app.presentation.fragment.login.auth

import com.acroninspector.app.presentation.fragment.login.base.BaseLoginView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface LoginView : BaseLoginView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openUserFunctionsScreen()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideKeyboard()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setDeviceId(deviceId: String)
}
