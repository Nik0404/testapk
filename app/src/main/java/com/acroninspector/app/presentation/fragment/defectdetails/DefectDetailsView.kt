package com.acroninspector.app.presentation.fragment.defectdetails

import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DefectDetailsView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setDefect(defectLog: DisplayDefectLog)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setToolbarTitle(defectName: String?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openRegisterDefectFragment(defectId: Int, entityType: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openMediaFilesFragment(entityId: Int, entityType: Int)
}