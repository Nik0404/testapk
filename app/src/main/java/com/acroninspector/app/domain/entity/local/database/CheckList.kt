package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "check_list")
data class CheckList(
        @PrimaryKey(autoGenerate = false) val id: Int,
        @ColumnInfo(name = "route_id") val routeId: Int,
        @ColumnInfo(name = "order_number") val orderNumber: Int,
        @ColumnInfo(name = "question_text") val questionText: String,
        @ColumnInfo(name = "value") val value: String,
        @ColumnInfo(name = "simbols_after_comma") val simbolsAfterComma: Int,
        @ColumnInfo(name = "min_value") val minValue: Double,
        @ColumnInfo(name = "max_value") val maxValue: Double,
        @ColumnInfo(name = "type_answer") val typeAnswer: Int,
        @ColumnInfo(name = "user_comment") val userComment: String,
        @ColumnInfo(name = "is_defect") val isDefect: Boolean,
        @ColumnInfo(name = "fact_end_date") val endDate: String
) : AcronEntity {

    @Ignore
    var attachmentsCount: Int = 0
}