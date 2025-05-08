package com.acroninspector.app.domain.interactors.search

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.SearchDefectHistory
import com.acroninspector.app.domain.entity.local.database.SearchEquipmentHistory
import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import com.acroninspector.app.domain.repositories.SearchRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class SearchInteractorImpl(
        private val searchRepository: SearchRepository,
        private val schedulersProvider: SchedulersProvider
) : SearchInteractor {

    override fun getSearchDefectHistoryEntries(): Single<List<DisplaySearchHistory>> {
        return searchRepository.getSearchDefectHistoryEntries()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getSearchedDefectHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>> {
        return searchRepository.getSearchedDefectHistoryEntries(query)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun insertDefectHistoryEntry(searchResult: String): Completable {
        val defectHistory = SearchDefectHistory(searchResult)

        return searchRepository.insertSearchDefectHistoryEntry(defectHistory)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getSearchEquipmentHistoryEntries(): Single<List<DisplaySearchHistory>> {
        return searchRepository.getSearchEquipmentHistoryEntries()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getSearchedEquipmentHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>> {
        return searchRepository.getSearchedEquipmentHistoryEntries(query)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun insertEquipmentHistoryEntry(searchResult: String): Completable {
        val equipmentHistory = SearchEquipmentHistory(searchResult)

        return searchRepository.insertSearchEquipmentHistoryEntry(equipmentHistory)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}