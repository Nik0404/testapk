package com.acroninspector.app.presentation.fragment.tasks

import androidx.annotation.StringRes
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface TasksView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateTasks(tasks: List<DisplayTask>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openTaskDetails(task: DisplayTask)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

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
}
