package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.DefectRelationColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.DefectRelation

class DefectRelationsMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return DefectRelation(
                scheme[DefectRelationColumns.CLASS_CODE_COLUMN_POSITION].toId(),
                scheme[DefectRelationColumns.DEFECT_ID_COLUMN_POSITION].toId(),
                scheme[DefectRelationColumns.DEFECT_CAUSE_ID_COLUMN_POSITION].toId()
        )
    }
}