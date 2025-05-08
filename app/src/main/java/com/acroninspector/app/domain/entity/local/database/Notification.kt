package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")
data class Notification(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "task_id") val taskId: Int,
        @ColumnInfo(name = "date_creation") val dateCreation: String,
        @ColumnInfo(name = "date_of_reading") val dateReading: String
) : AcronEntity
