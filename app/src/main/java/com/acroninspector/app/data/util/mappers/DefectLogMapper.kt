package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.DefectLogColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.DefectLog

class DefectLogMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return DefectLog(
            scheme[DefectLogColumns.ID_COLUMN_POSITION].toId(),
            scheme[DefectLogColumns.EQUIPMENT_ID_COLUMN_POSITION].toId(),
            scheme[DefectLogColumns.EQUIPMENT_NAME_COLUMN_POSITION],
            scheme[DefectLogColumns.EQUIPMENT_CODE_NAME_COLUMN_POSITION],
            scheme[DefectLogColumns.DEFECT_NAME_COLUMN_POSITION],
            scheme[DefectLogColumns.DEFECT_CAUSE_NAME_COLUMN_POSITION],
            scheme[DefectLogColumns.DEFECT_DT_COLUMN_POSITION],
            scheme[DefectLogColumns.DEFECT_CRITICALITY_COLUMN_POSITION].toId(),
            scheme[DefectLogColumns.DEFECT_NOTE_COLUMN_POSITION],
            scheme[DefectLogColumns.DEFECT_IS_CORRECTED_POSITION].toInt()
        )
    }
}
