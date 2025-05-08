package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.SearchHistoryDao
import com.acroninspector.app.domain.entity.local.database.SearchDefectHistory
import com.acroninspector.app.domain.entity.local.database.SearchEquipmentHistory
import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import com.acroninspector.app.domain.repositories.SearchRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class SearchRepositoryImpl(
        private val searchHistoryDao: SearchHistoryDao
): SearchRepository {

    override fun getSearchDefectHistoryEntries(): Single<List<DisplaySearchHistory>> {
        return searchHistoryDao.getAllDefectHistoryEntries().firstOrError()
    }

    override fun getSearchedDefectHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>> {
        return if (query.isEmpty()) {
            searchHistoryDao.getAllDefectHistoryEntries()
        } else searchHistoryDao.getSearchedDefectHistoryEntries(query)
    }

    override fun insertSearchDefectHistoryEntry(searchDefectHistory: SearchDefectHistory): Completable {
        return searchHistoryDao.insertSearchHistoryEntry(searchDefectHistory)
    }


    override fun getSearchEquipmentHistoryEntries(): Single<List<DisplaySearchHistory>> {
        return searchHistoryDao.getAllEquipmentHistoryEntries().firstOrError()
    }

    override fun getSearchedEquipmentHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>> {
        return if (query.isEmpty()) {
            searchHistoryDao.getAllEquipmentHistoryEntries()
        } else searchHistoryDao.getSearchedEquipmentHistoryEntries(query)
    }

    override fun insertSearchEquipmentHistoryEntry(searchEquipmentHistory: SearchEquipmentHistory): Completable {
        return searchHistoryDao.insertSearchHistoryEntry(searchEquipmentHistory)
    }
}