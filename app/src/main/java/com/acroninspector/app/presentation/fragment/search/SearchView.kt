package com.acroninspector.app.presentation.fragment.search

import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface SearchView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefectsFragment(searchQuery: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openEquipmentsFragment(searchQuery: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateSearchHistory(searchHistory: List<DisplaySearchHistory>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSearchQuery(searchQuery: String)

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

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeFragment()
}