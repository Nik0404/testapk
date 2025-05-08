package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.DefectColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Defect

class DefectMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return Defect(
                scheme[DefectColumns.ID_COLUMN_POSITION].toId(),
                scheme[DefectColumns.SHORT_NAME_COLUMN_POSITION],
                scheme[DefectColumns.FULL_NAME_COLUMN_POSITION]
        )
    }
}