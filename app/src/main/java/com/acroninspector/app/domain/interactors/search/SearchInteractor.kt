package com.acroninspector.app.domain.interactors.search

import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface SearchInteractor {

    fun getSearchDefectHistoryEntries(): Single<List<DisplaySearchHistory>>

    fun getSearchedDefectHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>>

    fun insertDefectHistoryEntry(searchResult: String): Completable


    fun getSearchedEquipmentHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>>

    fun getSearchEquipmentHistoryEntries(): Single<List<DisplaySearchHistory>>

    fun insertEquipmentHistoryEntry(searchResult: String): Completable
}