package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.RouteColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Route

class RouteMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return Route(
                scheme[RouteColumns.ID_COLUMN_POSITION].toId(),
                scheme[RouteColumns.TASK_ID_COLUMN_POSITION].toId(),
                scheme[RouteColumns.EQUIPMENT_ID_COLUMN_POSITION].toId(),
                scheme[RouteColumns.ORDER_NUMBER_COLUMN_POSITION].toInt(),
                scheme[RouteColumns.COUNT_NFC_COLUMN_POSITION].toInt(),
                getCountNfcAnswersFromSchema(scheme),
                scheme[RouteColumns.COUNT_QUESTION_COLUMN_POSITION].toInt(),
                getCountAnswersFromSchema(scheme),
                scheme[RouteColumns.FACT_START_DATE_COLUMN_POSITION],
                scheme[RouteColumns.FACT_END_DATE_COLUMN_POSITION]
        )
    }

    private fun getCountNfcAnswersFromSchema(scheme: List<String>): Int {
        return if (scheme[RouteColumns.COUNT_NFC_ANS_COLUMN_POSITION].isNotEmpty()) {
            scheme[RouteColumns.COUNT_NFC_ANS_COLUMN_POSITION].toInt()
        } else 0
    }

    private fun getCountAnswersFromSchema(scheme: List<String>): Int {
        return if (scheme[RouteColumns.COUNT_ANSWER_COLUMN_POSITION].isNotEmpty()) {
            scheme[RouteColumns.COUNT_ANSWER_COLUMN_POSITION].toInt()
        } else 0
    }
}
