package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.ExecutorColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Executor

class ExecutorMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return Executor(
                scheme[ExecutorColumns.EXECUTOR_ID_COLUMN_POSITION].toId(),
                scheme[ExecutorColumns.EXECUTOR_FULL_NAME_COLUMN_POSITION],
                scheme[ExecutorColumns.EXECUTOR_SHORT_NAME_COLUMN_POSITION],
                scheme[ExecutorColumns.EXECUTOR_GROUP_NUMBER_COLUMN_POSITION].toInt(),
                scheme[ExecutorColumns.EXECUTOR_GROUP_NAME_COLUMN_POSITION],
                scheme[ExecutorColumns.EXECUTOR_SUBDIVISION_ID_COLUMN_POSITION].toId()
        )
    }
}