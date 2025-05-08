package com.acroninspector.app.presentation.fragment.edittask

import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface EditTaskView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openControlProcedureFragment(taskId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateTask(task: DisplayTask)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showExecutorsDialog(executorGroup: Int, executorId: Int)
}