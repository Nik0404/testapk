package com.acroninspector.app.presentation.fragment.questions

import androidx.annotation.StringRes
import com.acroninspector.app.domain.entity.local.display.DisplayAnswer
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface QuestionsView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateQuestions(questions: List<DisplayQuestion>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openRegisterDefectFragment(defectLogId: Int, checkListId: Int, equipmentId: Int, taskId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openRegisterDefectFragment(equipmentId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openMediaFilesFragment(entityId: Int, entityType: Int, enableEditing: Boolean = false)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefectsFragment(entityId: Int, entityType: Int, couldEdit: Boolean = true)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openCommentFragment(comment: String, enableEditing: Boolean = false)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefectDetailsFragment(defectLog: DisplayDefectLog)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openQuestionsFragment(taskId: Int, taskStatus: Int, route: DisplayRoute)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSelectAnswerDialog(answers: List<DisplayAnswer>, position: Int,
                               onAnswerSelected: (String) -> Unit)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorDialog(@StringRes title: Int, @StringRes message: Int, text: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEquipmentByPassDialog(equipmentId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRoute(route: DisplayRoute)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideKeyboard()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeFragment()


    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openEquipmentDeffects(equipmentId: Int)

}