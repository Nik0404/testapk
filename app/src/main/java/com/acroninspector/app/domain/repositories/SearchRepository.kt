package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.SearchDefectHistory
import com.acroninspector.app.domain.entity.local.database.SearchEquipmentHistory
import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface SearchRepository {

    fun insertSearchDefectHistoryEntry(searchDefectHistory: SearchDefectHistory): Completable

    fun getSearchDefectHistoryEntries(): Single<List<DisplaySearchHistory>>

    fun getSearchedDefectHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>>


    fun insertSearchEquipmentHistoryEntry(searchEquipmentHistory: SearchEquipmentHistory): Completable

    fun getSearchEquipmentHistoryEntries(): Single<List<DisplaySearchHistory>>

    fun getSearchedEquipmentHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>>
}