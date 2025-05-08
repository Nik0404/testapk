package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.EquipmentColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Equipment

class EquipmentMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return Equipment(
                scheme[EquipmentColumns.ID_COLUMN_POSITION].toId(),
                scheme[EquipmentColumns.NAME_COLUMN_POSITION],
                scheme[EquipmentColumns.POSITION_CODE_COLUMN_POSITION],
                getEquipmentSchemaClassId(scheme),
                scheme[EquipmentColumns.DIRECTORY_ID_COLUMN_POSITION].toId(),
                scheme[EquipmentColumns.SUBDIVISION_COLUMN_POSITION],
                scheme[EquipmentColumns.BUILDING_COLUMN_POSITION],
                scheme[EquipmentColumns.ADDITIONAL_INFO_COLUMN_POSITION]
        )
    }

    private fun getEquipmentSchemaClassId(scheme: List<String>): Int {
        return if (scheme[EquipmentColumns.EQUIPMENT_CLASS_ID_COLUMN_POSITION].isNotEmpty()) {
            scheme[EquipmentColumns.EQUIPMENT_CLASS_ID_COLUMN_POSITION].toId()
        } else -1
    }
}
