package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.NfcEquipmentColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.NfcEquipment

class NfcEquipmentMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return NfcEquipment(
                scheme[NfcEquipmentColumns.ID_COLUMN_POSITION].toId(),
                scheme[NfcEquipmentColumns.EQUIPMENT_ID_COLUMN_POSITION].toId(),
                scheme[NfcEquipmentColumns.NAME_COLUMN_POSITION],
                scheme[NfcEquipmentColumns.CODE_COLUMN_POSITION]
        )
    }
}
