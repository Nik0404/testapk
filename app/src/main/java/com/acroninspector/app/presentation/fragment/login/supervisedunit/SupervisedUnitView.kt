package com.acroninspector.app.presentation.fragment.login.supervisedunit

import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface SupervisedUnitView : BaseLoginView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDivisions(divisions: List<Division>)
}
