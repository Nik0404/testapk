package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.DefectCauseColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.DefectCause

class DefectCauseMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return DefectCause(
                scheme[DefectCauseColumns.ID_COLUMN_POSITION].toId(),
                scheme[DefectCauseColumns.SHORT_NAME_COLUMN_POSITION],
                scheme[DefectCauseColumns.FULL_NAME_COLUMN_POSITION]
        )
    }
}