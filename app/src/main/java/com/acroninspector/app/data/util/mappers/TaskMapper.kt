package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.TaskColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Task

class TaskMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        val task = Task(
                scheme[TaskColumns.TASK_ID_COLUMN_POSITION].toId(),
                scheme[TaskColumns.SUBDIVISION_ID_COLUMN_POSITION].toId(),
                scheme[TaskColumns.TASK_ID_COLUMN_POSITION],
                scheme[TaskColumns.TASK_NAME_COLUMN_POSITION],
                getStatusCode(scheme),
                scheme[TaskColumns.EXECUTOR_ID_COLUMN_POSITION].toId(),
                scheme[TaskColumns.EXECUTOR_GROUP_ID_COLUMN_POSITION].toId(),
                scheme[TaskColumns.START_DATE_PLAN_COLUMN_POSITION],
                scheme[TaskColumns.END_DATE_PLAN_COLUMN_POSITION],
                scheme[TaskColumns.START_DATE_FACT_COLUMN_POSITION],
                scheme[TaskColumns.END_DATE_FACT_COLUMN_POSITION],
                getUnasweredChecklistsCountFromSchema(scheme),
                getCountChecklistsCountFromSchema(scheme),
                scheme[TaskColumns.USER_COMMENT_COLUMN_POSITION],
                scheme[TaskColumns.NOTIFICATION_DATE_COLUMN_POSITION],
                scheme[TaskColumns.IMEC_BEG_COLUMN_POSITION],
                scheme[TaskColumns.IMEC_END_COLUMN_POSITION]
        )
        task.attachmentsCount = getAttachmentsCountFromSchema(scheme)

        return task
    }

    private fun getAttachmentsCountFromSchema(scheme: List<String>): Int {
        return if (scheme[TaskColumns.DOCUMENT_FLAG_COLUMN_POSITION].isNotEmpty()) {
            scheme[TaskColumns.DOCUMENT_FLAG_COLUMN_POSITION].toInt()
        } else 0
    }

    private fun getUnasweredChecklistsCountFromSchema(scheme: List<String>): Int {
        return if (scheme[TaskColumns.UNANSWERED_CHECKLISTS_COLUMN_POSITION].isNotEmpty()) {
            scheme[TaskColumns.UNANSWERED_CHECKLISTS_COLUMN_POSITION].toInt()
        } else 0
    }

    private fun getCountChecklistsCountFromSchema(scheme: List<String>): Int {
        return if (scheme[TaskColumns.COUNT_CHECKLISTS_COLUMN_POSITION].isNotEmpty()) {
            scheme[TaskColumns.COUNT_CHECKLISTS_COLUMN_POSITION].toInt()
        } else 0
    }

    private fun getStatusCode(scheme: List<String>): Int {
        return if (scheme[TaskColumns.STATUS_CODE_COLUMN_POSITION].isNotEmpty()) {
            scheme[TaskColumns.STATUS_CODE_COLUMN_POSITION].toInt()
        } else DatabaseConstants.TASK_STATUS_NEW
    }
}
