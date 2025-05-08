package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer")
data class Answer(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "check_list_id") val checkListId: Int,
    @ColumnInfo(name = "order_number") val orderNumber: Int,
    @ColumnInfo(name = "answer_text") val answerText: String,
    @ColumnInfo(name = "is_defect") val isDefect: Boolean,
    @ColumnInfo(name = "is_default") val isDefault: Boolean
) : AcronEntity