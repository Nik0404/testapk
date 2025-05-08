package com.acroninspector.app.presentation.fragment.defects

import androidx.annotation.StringRes
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DefectsBaseView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefectDetails(defect: DisplayDefectLog)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateDefects(defectLogs: List<DisplayDefectLog>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun prepareEmptyState(@StringRes emptyStateMessageResId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmptyState()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideEmptyState()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)
}