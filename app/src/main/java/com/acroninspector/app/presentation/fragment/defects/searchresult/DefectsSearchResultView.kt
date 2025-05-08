package com.acroninspector.app.presentation.fragment.defects.searchresult

import androidx.annotation.StringRes
import com.acroninspector.app.presentation.fragment.defects.DefectsBaseView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DefectsSearchResultView : DefectsBaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSearchEmptyState(@StringRes emptyStateMessageResId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setEquipmentEmptyState(@StringRes emptyStateMessageResId: Int)
}