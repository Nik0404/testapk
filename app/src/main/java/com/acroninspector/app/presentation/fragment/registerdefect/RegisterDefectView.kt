package com.acroninspector.app.presentation.fragment.registerdefect

import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface RegisterDefectView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setAttachmentsCount(count: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setComment(comment: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setEquipment(name: String, code: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCriticality(criticality: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setDefectCauseName(defectCauseName: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun resetDefectCauseName()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setDefectName(defectName: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openMediaFilesFragment(entityId: Int, entityType: Int, isCreatingDefect: Boolean = false)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openCommentFragment(defectId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefectCauseFragment(defectNameId: Int, equipmentClassId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefectNameFragment(equipmentClassId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCriticalityDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeFragment()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)
}