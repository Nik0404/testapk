package com.acroninspector.app.presentation.fragment.taskdetails.bypass

import android.os.Bundle
import androidx.annotation.StringRes
import com.acroninspector.app.presentation.fragment.taskdetails.TaskDetailsView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface TaskDetailsByPassView : TaskDetailsView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openQuestionsFragment(args: Bundle)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openTaskCommentFragment(args: Bundle)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openMediaFilesFragment(args: Bundle)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefectsFragment(args: Bundle)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openRegisterDefectFragment(equipmentId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showStartRouteDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showUnfinishedTaskDialog(event: Int, unansweredItems: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorDialog(@StringRes title: Int, @StringRes message: Int, text: String = "")

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEquipmentByPassDialog(equipmentId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showRouteButton()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideRouteButton()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openRouteDefectsFragment(args: Bundle)


}