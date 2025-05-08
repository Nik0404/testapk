package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.NfcRouteColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.NfcRoute

class NfcRouteMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return NfcRoute(
                scheme[NfcRouteColumns.ID_COLUMN_POSITION].toId(),
                scheme[NfcRouteColumns.ROUTE_ID_COLUMN_POSITION].toId(),
                scheme[NfcRouteColumns.NAME_COLUMN_POSITION],
                scheme[NfcRouteColumns.CODE_COLUMN_POSITION],
                scheme[NfcRouteColumns.TIME_COLUMN_POSITION]
        )
    }
}
