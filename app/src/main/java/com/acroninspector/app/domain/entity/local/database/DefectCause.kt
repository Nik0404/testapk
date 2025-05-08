package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "defect_cause")
data class DefectCause(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "code") val id: Int,
        @ColumnInfo(name = "short_name") val shortName: String,
        @ColumnInfo(name = "full_name") val fullName: String
) : AcronEntity