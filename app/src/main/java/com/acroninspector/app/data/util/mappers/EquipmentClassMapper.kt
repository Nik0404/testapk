package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.EquipmentClassColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.EquipmentClass

class EquipmentClassMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return EquipmentClass(
                scheme[EquipmentClassColumns.ID_COLUMN_POSITION].toId(),
                scheme[EquipmentClassColumns.SHORT_NAME_COLUMN_POSITION],
                scheme[EquipmentClassColumns.FULL_NAME_COLUMN_POSITION]
        )
    }
}
