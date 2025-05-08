package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.TaskColumns
import com.google.gson.annotations.SerializedName

data class TaskStatusValue(
    @SerializedName(TaskColumns.TASK_ID_COLUMN_NAME) val taskId: String,
    @SerializedName(TaskColumns.STATUS_CODE_COLUMN_NAME) val taskStatusCode: String,
    @SerializedName(TaskColumns.START_DATE_FACT_COLUMN_NAME) val startDateFact: String,
    @SerializedName(TaskColumns.NOTIFICATION_DATE_COLUMN_NAME) val notificationDate: String
    ) : AcronValue