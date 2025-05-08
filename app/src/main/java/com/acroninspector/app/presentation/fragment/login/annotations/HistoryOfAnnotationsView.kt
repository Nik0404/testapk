package com.acroninspector.app.presentation.fragment.login.annotations;

import com.acroninspector.app.domain.entity.local.database.Releases
import com.acroninspector.app.domain.entity.local.display.DisplayAnnotation
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface HistoryOfAnnotationsView: BaseLoginView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openFunctionFragment()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showAnnotations(annotation: List<DisplayAnnotation>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun displayReleaseDetails(details: String)


}
