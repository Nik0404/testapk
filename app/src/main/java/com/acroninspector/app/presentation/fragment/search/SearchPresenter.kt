package com.acroninspector.app.presentation.fragment.search

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import com.acroninspector.app.domain.interactors.search.SearchInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber

@InjectViewState
class SearchPresenter(
        private val searchInteractor: SearchInteractor
) : BasePresenter<SearchView>() {

    private var searchHistoryEntries: List<DisplaySearchHistory> = ArrayList()

    var searchEntity = Constants.DEFAULT_INVALID_TYPE

    private val setSearchHistoryToAdapterConsumer: ((it: List<DisplaySearchHistory>) -> Unit) = {
        viewState.hideProgress()
        viewState.updateSearchHistory(it)

        searchHistoryEntries = it
    }

    private val showErrorConsumer: ((it: Throwable) -> Unit) = {
        viewState.hideProgress()
        viewState.showSnackbar(R.string.error_message)

        Timber.e(it)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadSearchHistory()
    }

    private fun loadSearchHistory() {
        viewState.showProgress()
        subscriptions.add(handleLoadingAllHistoryEntries()
                .subscribe(setSearchHistoryToAdapterConsumer, showErrorConsumer))
    }

    fun search(query: String) {
        viewState.showProgress()
        subscriptions.add(handleLoadingSearchedHistoryEntries(query)
                .subscribe(setSearchHistoryToAdapterConsumer, showErrorConsumer))
    }

    fun onSearchHistoryItemClicked(position: Int) {
        val searchQuery = searchHistoryEntries[position].entry
        handleOpeningFragment(searchQuery)

        viewState.setSearchQuery(searchQuery)
    }

    fun onSearchButtonClicked(searchQuery: String) {
        if (searchQuery.isNotEmpty()) {
            viewState.setSearchQuery(searchQuery)

            if (isSearchQueryExists(searchQuery)) {
                handleOpeningFragment(searchQuery)
            } else {
                subscriptions.add(handleInsertingSearchHistoryEntry(searchQuery)
                        .subscribe({
                            handleOpeningFragment(searchQuery)
                        }, {
                            handleOpeningFragment(searchQuery)

                            viewState.showToast(R.string.error_message)
                        })
                )
            }
        }
    }

    private fun isSearchQueryExists(searchQuery: String): Boolean {
        var isExists = false
        searchHistoryEntries.forEach { searchHistoryEntry ->
            if (searchHistoryEntry.entry == searchQuery) {
                isExists = true
                return isExists
            }
        }
        return isExists
    }

    private fun handleOpeningFragment(searchQuery: String) {
        when (searchEntity) {
            Constants.ENTITY_DEFECT_LOG -> viewState.openDefectsFragment(searchQuery)
            Constants.ENTITY_EQUIPMENT -> viewState.openEquipmentsFragment(searchQuery)
            else -> throw IllegalArgumentException("Unknown entity: $searchEntity")
        }
    }

    private fun handleInsertingSearchHistoryEntry(searchResult: String): Completable {
        return when (searchEntity) {
            Constants.ENTITY_DEFECT_LOG -> searchInteractor.insertDefectHistoryEntry(searchResult)
            Constants.ENTITY_EQUIPMENT -> searchInteractor.insertEquipmentHistoryEntry(searchResult)
            else -> throw IllegalArgumentException("Unknown entity: $searchEntity")
        }
    }

    private fun handleLoadingSearchedHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>> {
        return when (searchEntity) {
            Constants.ENTITY_DEFECT_LOG -> searchInteractor.getSearchedDefectHistoryEntries(query)
            Constants.ENTITY_EQUIPMENT -> searchInteractor.getSearchedEquipmentHistoryEntries(query)
            else -> throw IllegalArgumentException("Unknown entity: $searchEntity")
        }
    }

    private fun handleLoadingAllHistoryEntries(): Single<List<DisplaySearchHistory>> {
        return when (searchEntity) {
            Constants.ENTITY_DEFECT_LOG -> searchInteractor.getSearchDefectHistoryEntries()
            Constants.ENTITY_EQUIPMENT -> searchInteractor.getSearchEquipmentHistoryEntries()
            else -> throw IllegalArgumentException("Unknown entity: $searchEntity")
        }
    }
}
