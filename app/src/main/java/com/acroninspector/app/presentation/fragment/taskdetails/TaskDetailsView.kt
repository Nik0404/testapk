package com.acroninspector.app.presentation.fragment.taskdetails

import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface TaskDetailsView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateRoutes(routes: List<DisplayRoute>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTask(task: DisplayTask)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)
}