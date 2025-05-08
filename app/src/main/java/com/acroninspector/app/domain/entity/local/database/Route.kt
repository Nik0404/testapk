package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route")
data class Route(
        @PrimaryKey(autoGenerate = false) val id: Int,
        @ColumnInfo(name = "task_id") val taskId: Int,
        @ColumnInfo(name = "equipment_id") val equipmentId: Int,
        @ColumnInfo(name = "order_number") val orderNumber: Int,
        @ColumnInfo(name = "count_nfc") val countNfc: Int,
        @ColumnInfo(name = "count_nfc_answered") val countNfcAnswered: Int,
        @ColumnInfo(name = "count_question") val countQuestion: Int,
        @ColumnInfo(name = "count_answer") val countAnswer: Int,
        @ColumnInfo(name = "fact_start_date") val factStartDate: String,
        @ColumnInfo(name = "fact_end_date") val factEndDate: String
) : AcronEntity
