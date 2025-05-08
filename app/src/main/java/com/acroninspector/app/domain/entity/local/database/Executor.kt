package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "executor", primaryKeys = ["executor_id", "group_number", "subdivision_id"])
data class Executor(
        @ColumnInfo(name = "executor_id") val id: Int,
        @ColumnInfo(name = "full_name") val fullName: String,
        @ColumnInfo(name = "short_name") val shortName: String,
        @ColumnInfo(name = "group_number") val groupNumber: Int,
        @ColumnInfo(name = "group_name") val groupName: String,
        @ColumnInfo(name = "subdivision_id") val divisionId: Int
) : AcronEntity