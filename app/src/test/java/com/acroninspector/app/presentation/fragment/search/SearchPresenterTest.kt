package com.acroninspector.app.presentation.fragment.search

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import com.acroninspector.app.domain.interactors.search.SearchInteractor
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
class SearchPresenterTest {

    private lateinit var presenter: SearchPresenter

    @Mock
    lateinit var interactor: SearchInteractor

    @Mock
    lateinit var viewState: `SearchView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = SearchPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeSearchEntries(): List<DisplaySearchHistory> {
        return arrayListOf(
                DisplaySearchHistory("1"),
                DisplaySearchHistory("2"),
                DisplaySearchHistory("3")
        )
    }

    private fun verifySuccessLoading(data: List<DisplaySearchHistory>) {
        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateSearchHistory(data)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    private fun verifyErrorLoading() {
        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateSearchHistory(ArgumentMatchers.anyList())
    }

    @Test
    fun loadSearchHistory_Success() {
        val methodName = "loadSearchHistory"
        val searchEntries = getFakeSearchEntries()

        `when`(interactor.getSearchDefectHistoryEntries())
                .thenReturn(Single.just(getFakeSearchEntries()))

        presenter.searchEntity = Constants.ENTITY_DEFECT_LOG
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchDefectHistoryEntries()
        verifySuccessLoading(searchEntries)
    }

    @Test
    fun loadSearchHistory_Failed() {
        val methodName = "loadSearchHistory"

        `when`(interactor.getSearchDefectHistoryEntries())
                .thenReturn(Single.error(Throwable()))

        presenter.searchEntity = Constants.ENTITY_DEFECT_LOG
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchDefectHistoryEntries()
        verifyErrorLoading()
    }

    @Test
    fun search_Success() {
        val methodName = "search"
        val query = "search query"
        val searchEntries = getFakeSearchEntries()

        `when`(interactor.getSearchedDefectHistoryEntries(query))
                .thenReturn(Flowable.just(getFakeSearchEntries()))

        presenter.searchEntity = Constants.ENTITY_DEFECT_LOG
        Whitebox.invokeMethod<Void>(presenter, methodName, query)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedDefectHistoryEntries(query)
        verifySuccessLoading(searchEntries)
    }

    @Test
    fun search_Failed() {
        val methodName = "search"
        val query = "search query"

        `when`(interactor.getSearchedDefectHistoryEntries(query))
                .thenReturn(Flowable.error(Throwable()))

        presenter.searchEntity = Constants.ENTITY_DEFECT_LOG
        Whitebox.invokeMethod<Void>(presenter, methodName, query)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedDefectHistoryEntries(query)
        verifyErrorLoading()
    }

    @Test
    fun onSearchHistoryItemClicked_DefectLog() {
        val searchEntries = getFakeSearchEntries()
        val position = 1

        val searchQuery = searchEntries[position].entry

        Whitebox.setInternalState(presenter, "searchHistoryEntries", searchEntries)
        presenter.searchEntity = Constants.ENTITY_DEFECT_LOG
        presenter.onSearchHistoryItemClicked(position)

        verify(viewState).openDefectsFragment(searchQuery)
        verify(viewState, never()).openEquipmentsFragment(searchQuery)
    }

    @Test
    fun onSearchHistoryItemClicked_Equipment() {
        val searchEntries = getFakeSearchEntries()
        val position = 1

        val searchQuery = searchEntries[position].entry

        Whitebox.setInternalState(presenter, "searchHistoryEntries", searchEntries)
        presenter.searchEntity = Constants.ENTITY_EQUIPMENT
        presenter.onSearchHistoryItemClicked(position)

        verify(viewState).openEquipmentsFragment(searchQuery)
        verify(viewState, never()).openDefectsFragment(searchQuery)
    }


    @Test
    fun onSearchButtonClicked_Success() {
        val methodName = "onSearchButtonClicked"
        val query = "search query"

        `when`(interactor.insertDefectHistoryEntry(query))
                .thenReturn(Completable.complete())

        presenter.searchEntity = Constants.ENTITY_DEFECT_LOG
        Whitebox.invokeMethod<Void>(presenter, methodName, query)

        verify(viewState).openDefectsFragment(query)
        verify(viewState, never()).openEquipmentsFragment(query)
        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun onSearchButtonClicked_Failed() {
        val methodName = "onSearchButtonClicked"
        val query = "search query"

        `when`(interactor.insertDefectHistoryEntry(query))
                .thenReturn(Completable.error(Throwable()))

        presenter.searchEntity = Constants.ENTITY_DEFECT_LOG
        Whitebox.invokeMethod<Void>(presenter, methodName, query)

        verify(viewState).openDefectsFragment(query)
        verify(viewState).showToast(R.string.error_message)
        verify(viewState, never()).openEquipmentsFragment(query)
    }

    @Test
    fun isSearchQueryExists() {
        val searchHistoryEntries = getFakeSearchEntries()
        val existingSearchQuery = "1"
        val newSearchQuery = "4"

        Whitebox.setInternalState(presenter, "searchHistoryEntries", searchHistoryEntries)
        val isExistingSearchQueryExists =
                Whitebox.invokeMethod<Boolean>(presenter, "isSearchQueryExists", existingSearchQuery)
        val isNewSearchQueryExists =
                Whitebox.invokeMethod<Boolean>(presenter, "isSearchQueryExists", newSearchQuery)

        assertTrue(isExistingSearchQueryExists)
        assertFalse(isNewSearchQueryExists)
    }
}