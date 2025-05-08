package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.common.extension.toMaxValue
import com.acroninspector.app.common.extension.toMinValue
import com.acroninspector.app.data.util.constants.CheckListColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.CheckList

class CheckListMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        val checkList = CheckList(
                scheme[CheckListColumns.ID_COLUMN_POSITION].toId(),
                scheme[CheckListColumns.ROUTE_ID_COLUMN_POSITION].toId(),
                scheme[CheckListColumns.ORDER_NUMBER_COLUMN_POSITION].toInt(),
                scheme[CheckListColumns.QUESTION_TEXT_COLUMN_POSITION],
                mapAnswer(scheme),
                mapSimbolsAfterComma(scheme),
                scheme[CheckListColumns.MIN_VALUE_COLUMN_POSITION].toMinValue(),
                scheme[CheckListColumns.MAX_VALUE_COLUMN_POSITION].toMaxValue(),
                scheme[CheckListColumns.TYPE_ANSWER_COLUMN_POSITION].toInt(),
                scheme[CheckListColumns.USER_COMMENT_COLUMN_POSITION],
                mapHasDefect(scheme),
                scheme[CheckListColumns.FACT_END_DATE_COLUMN_POSITION]
        )
        checkList.attachmentsCount = getAttachmentsCountFromSchema(scheme)

        return checkList
    }

    private fun getAttachmentsCountFromSchema(scheme: List<String>): Int {
        return if (scheme[CheckListColumns.COUNT_ATTACHEMENTS_COLUMN_POSITION].isNotEmpty()) {
            scheme[CheckListColumns.COUNT_ATTACHEMENTS_COLUMN_POSITION].toInt()
        } else 0
    }

    private fun mapSimbolsAfterComma(scheme: List<String>): Int {
        return if (scheme[CheckListColumns.SIMBOLS_AFTER_COMMA_COLUMN_POSITION].isNotEmpty()) {
            scheme[CheckListColumns.SIMBOLS_AFTER_COMMA_COLUMN_POSITION].toInt()
        } else 0
    }

    private fun mapAnswer(scheme: List<String>): String {
        return if (scheme[CheckListColumns.TYPE_ANSWER_COLUMN_POSITION].toInt()
                == DatabaseConstants.TYPE_ANSWER_VALUE) {
            scheme[CheckListColumns.VALUE_NUM_COLUMN_POSITION]
        } else {
            scheme[CheckListColumns.VALUE_COLUMN_POSITION]
        }
    }

    private fun mapHasDefect(scheme: List<String>): Boolean {
        return !(scheme[CheckListColumns.HAS_DEFECT_COLUMN_POSITION].isEmpty() ||
                scheme[CheckListColumns.HAS_DEFECT_COLUMN_POSITION] == "0")
    }
}
