package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.TaskColumns
import com.google.gson.annotations.SerializedName

class TaskExecutorValue(
        @SerializedName(TaskColumns.TASK_ID_COLUMN_NAME) val taskId: String,
        @SerializedName(TaskColumns.EXECUTOR_ID_COLUMN_NAME) val executorId: String
) : AcronValue