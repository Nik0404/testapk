package com.acroninspector.app.presentation.fragment.nfc.nfcname

import androidx.annotation.IdRes
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface NfcNameView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeFragment(@IdRes destinationId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showKeyboard()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideKeyboard()
}