package com.acroninspector.app.data.util.constants

object CheckListColumns {

    const val ID_COLUMN_NAME = "CHECKLIST_ID"
    const val ID_COLUMN_POSITION = 0

    const val ROUTE_ID_COLUMN_NAME = "ROUTE_OBJECT_ID"
    const val ROUTE_ID_COLUMN_POSITION = 1

    const val ORDER_NUMBER_COLUMN_NAME = "ORDER_NUMBER"
    const val ORDER_NUMBER_COLUMN_POSITION = 2

    const val QUESTION_TEXT_COLUMN_NAME = "OPERATION"
    const val QUESTION_TEXT_COLUMN_POSITION = 3

    const val VALUE_COLUMN_NAME = "VALUE"
    const val VALUE_COLUMN_POSITION = 4

    const val MIN_VALUE_COLUMN_NAME = "MIN_VALUE"
    const val MIN_VALUE_COLUMN_POSITION = 5

    const val MAX_VALUE_COLUMN_NAME = "MAX_VALUE"
    const val MAX_VALUE_COLUMN_POSITION = 6

    const val USER_COMMENT_COLUMN_NAME = "USER_COMMENT"
    const val USER_COMMENT_COLUMN_POSITION = 7

    const val TYPE_ANSWER_COLUMN_NAME = "TYPE_ANSWER"
    const val TYPE_ANSWER_COLUMN_POSITION = 8

    const val HAS_DEFECT_COLUMN_NAME = "DEFECT_FLAG"
    const val HAS_DEFECT_COLUMN_POSITION = 9

    const val FACT_END_DATE_COLUMN_NAME = "FACT_END_DATE"
    const val FACT_END_DATE_COLUMN_POSITION = 10

    const val VALUE_NUM_COLUMN_NAME = "VALUE_NUM"
    const val VALUE_NUM_COLUMN_POSITION = 11

    const val COUNT_ATTACHMENTS_COLUMN_NAME = "COUNT_ATTACHMENT"
    const val COUNT_ATTACHEMENTS_COLUMN_POSITION = 12

    const val SIMBOLS_AFTER_COMMA_COLUMN_NAME = "NDP"
    const val SIMBOLS_AFTER_COMMA_COLUMN_POSITION = 13

    fun getColumns(): List<String> {
        return listOf(
                ID_COLUMN_NAME,
                ROUTE_ID_COLUMN_NAME,
                ORDER_NUMBER_COLUMN_NAME,
                QUESTION_TEXT_COLUMN_NAME,
                VALUE_COLUMN_NAME,
                MIN_VALUE_COLUMN_NAME,
                MAX_VALUE_COLUMN_NAME,
                USER_COMMENT_COLUMN_NAME,
                TYPE_ANSWER_COLUMN_NAME,
                HAS_DEFECT_COLUMN_NAME,
                FACT_END_DATE_COLUMN_NAME,
                VALUE_NUM_COLUMN_NAME,
                COUNT_ATTACHMENTS_COLUMN_NAME,
                SIMBOLS_AFTER_COMMA_COLUMN_NAME
        )
    }
}
