package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment_class")
data class EquipmentClass(
        @PrimaryKey(autoGenerate = false) val id: Int,
        @ColumnInfo(name = "short_name") val shortName: String,
        @ColumnInfo(name = "full_name") val fullName: String
) : AcronEntity
