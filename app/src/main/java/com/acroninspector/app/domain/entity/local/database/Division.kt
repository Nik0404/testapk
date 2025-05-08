package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "division")
data class Division(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "division_name") val name: String
) : AcronEntity
