package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
        @PrimaryKey(autoGenerate = false) val id: Int,
        @ColumnInfo(name = "subdivision_id") val subdivisionId: Int,
        @ColumnInfo(name = "task_number") val taskNumber: String,
        @ColumnInfo(name = "task_name") val taskName: String,
        @ColumnInfo(name = "task_status_code") val taskStatusCode: Int,
        @ColumnInfo(name = "executor_id") val executorId: Int,
        @ColumnInfo(name = "executor_group_id") val executorGroupId: Int,
        @ColumnInfo(name = "start_date_plan") val startDatePlan: String,
        @ColumnInfo(name = "end_date_plan") val endDatePlan: String,
        @ColumnInfo(name = "start_date_fact") val startDateFact: String,
        @ColumnInfo(name = "end_date_fact") val endDateFact: String,
        @ColumnInfo(name = "unanswered_checklist") val unansweredChecklist: Int,
        @ColumnInfo(name = "count_checklist") val countChecklist: Int,
        @ColumnInfo(name = "user_comment") val userComment: String,
        @ColumnInfo(name = "notification_date") val notificationDate: String,
        @ColumnInfo(name = "device_id_on_start") val deviceIdOnStart: String,
        @ColumnInfo(name = "device_id_on_finish") val deviceIdOnFinish: String
) : AcronEntity {

    @Ignore
    var attachmentsCount: Int = 0
}
