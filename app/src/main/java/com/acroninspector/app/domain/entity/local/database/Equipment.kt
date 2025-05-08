package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment")
data class Equipment(
        @PrimaryKey(autoGenerate = false) val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "code") val code: String,
        @ColumnInfo(name = "equipment_class_id") val equipmentClassId: Int,
        @ColumnInfo(name = "directory_id") val directoryId: Int,
        @ColumnInfo(name = "subdivision") val subdivision: String,
        @ColumnInfo(name = "building") val building: String,
        @ColumnInfo(name = "additional_info") val additionalInfo: String
) : AcronEntity
