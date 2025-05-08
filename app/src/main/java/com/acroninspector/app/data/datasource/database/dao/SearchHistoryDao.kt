package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.SearchDefectHistory
import com.acroninspector.app.domain.entity.local.database.SearchEquipmentHistory
import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchHistoryEntry(entry: SearchDefectHistory): Completable

    @Query("SELECT search_entry AS entry FROM search_defect_history ORDER BY id DESC")
    fun getAllDefectHistoryEntries(): Flowable<List<DisplaySearchHistory>>

    @Query("""
        SELECT search_entry AS entry
        FROM search_defect_history
        WHERE search_entry LIKE '%' || :query || '%'
        ORDER BY id DESC
    """)
    fun getSearchedDefectHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchHistoryEntry(entry: SearchEquipmentHistory): Completable

    @Query("SELECT search_entry AS entry FROM search_equipment_history ORDER BY id DESC")
    fun getAllEquipmentHistoryEntries(): Flowable<List<DisplaySearchHistory>>

    @Query("""
        SELECT search_entry AS entry
        FROM search_equipment_history
        WHERE search_entry LIKE '%' || :query || '%'
        ORDER BY id DESC
    """)
    fun getSearchedEquipmentHistoryEntries(query: String): Flowable<List<DisplaySearchHistory>>
}