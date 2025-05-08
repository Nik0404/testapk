package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "function")
data class UserFunction(
        @ColumnInfo(name = "function_code") @PrimaryKey(autoGenerate = false) val functionCode: Int,
        @ColumnInfo(name = "function_name") val functionName: String,
        @ColumnInfo(name = "function_title") val functionTitle: String
)
