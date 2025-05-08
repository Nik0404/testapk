package com.acroninspector.app.presentation.fragment.nfc.definenfc

import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DefineNfcView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openNfcNameFragment(nfcEquipmentId: Int, nfcCode: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setNfcMark(nfcCode: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dropNfcMark()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorDialog()
}