package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.display.DisplayDirectory
import io.reactivex.Flowable

@Dao
interface DirectoryDao {

    @Query("""
        SELECT id, name, parent_id AS parentId, end_level AS endLvl
        FROM directory
        WHERE parent_id = :parentId
        ORDER BY directory.name ASC
    """)
    fun getDirectories(parentId: Int): Flowable<List<DisplayDirectory>>

    @Query("""
        SELECT id, name, directory.parent_id AS parentId, directory.end_level AS endLvl
        FROM directory
        WHERE directory.name LIKE '%' || :query || '%'
        ORDER BY directory.name ASC
    """)
    fun getSearchedDirectories(query: String): Flowable<List<DisplayDirectory>>
}
