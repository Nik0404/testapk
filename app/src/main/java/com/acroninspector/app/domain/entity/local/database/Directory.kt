package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "directory")
class Directory(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sign") val sign: String,
    @ColumnInfo(name = "parent_id") val parentId: Int,
    @ColumnInfo(name = "end_level") val endLevel: Boolean
) : AcronEntity
