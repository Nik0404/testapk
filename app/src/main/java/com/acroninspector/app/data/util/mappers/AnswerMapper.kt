package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.AnswerColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Answer

class AnswerMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return Answer(
                scheme[AnswerColumns.ID_COLUMN_POSITION].toId(),
                scheme[AnswerColumns.CHECKLIST_ID_COLUMN_POSITION].toId(),
                scheme[AnswerColumns.ORDER_NUMBER_COLUMN_POSITION].toInt(),
                scheme[AnswerColumns.ANSWER_TEXT_COLUMN_POSITION],
                mapIsDefect(scheme), mapIsDefault(scheme)
        )
    }

    private fun mapIsDefect(scheme: List<String>): Boolean {
        return !(scheme[AnswerColumns.IS_DEFECT_COLUMN_POSITION].isEmpty() ||
                scheme[AnswerColumns.IS_DEFECT_COLUMN_POSITION] == "0")
    }

    private fun mapIsDefault(scheme: List<String>): Boolean {
        return !(scheme[AnswerColumns.IS_DEFAULT_COLUMN_POSITION].isEmpty() ||
                scheme[AnswerColumns.IS_DEFAULT_COLUMN_POSITION] == "0")
    }
}