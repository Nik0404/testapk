package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.TaskColumns
import com.acroninspector.app.presentation.dialog.StartRouteDialog
import com.google.gson.annotations.SerializedName

data class TaskValue(
        @SerializedName(TaskColumns.TASK_ID_COLUMN_NAME) val taskId: String,
        @SerializedName(TaskColumns.STATUS_CODE_COLUMN_NAME) val taskStatusCode: String,
        @SerializedName(TaskColumns.START_DATE_FACT_COLUMN_NAME) val startDateFact: String,
        @SerializedName(TaskColumns.END_DATE_FACT_COLUMN_NAME) val endDateFact: String,
        @SerializedName(TaskColumns.UNANSWERED_CHECKLISTS_COLUMN_NAME) val unansweredChecklist: String,
        @SerializedName(TaskColumns.USER_COMMENT_COLUMN_NAME) val userComment: String,
        @SerializedName(TaskColumns.NOTIFICATION_DATE_COLUMN_NAME) val notificationDate: String,
        @SerializedName(TaskColumns.DEFECT_FLAG_COLUMN_NAME) val defectFlag: String,
        @SerializedName(TaskColumns.DOCUMENT_FLAG_COLUMN_NAME) val documentFlag: String,
        @SerializedName(TaskColumns.IMEC_BEG_COLUMN_NAME) val deviceIdOnStart: String,
        @SerializedName(TaskColumns.IMEC_END_COLUMN_NAME) val deviceIdOnFinish: String
) : AcronValue