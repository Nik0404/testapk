package com.acroninspector.app.presentation.fragment.taskcomment

import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface TaskCommentView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setTask(task: DisplayTask)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeFragment()

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