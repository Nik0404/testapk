package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_defect_history")
data class SearchDefectHistory(
    @ColumnInfo(name = "search_entry") val entry: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}