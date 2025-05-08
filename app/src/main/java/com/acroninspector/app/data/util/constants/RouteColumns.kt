package com.acroninspector.app.data.util.constants

object RouteColumns {

    const val ID_COLUMN_NAME = "ROUTE_OBJECT_ID"
    const val ID_COLUMN_POSITION = 0

    const val TASK_ID_COLUMN_NAME = "TASK_ID"
    const val TASK_ID_COLUMN_POSITION = 1

    const val EQUIPMENT_ID_COLUMN_NAME = "POSITION_ID"
    const val EQUIPMENT_ID_COLUMN_POSITION = 2

    const val ORDER_NUMBER_COLUMN_NAME = "ORDER_NUMBER"
    const val ORDER_NUMBER_COLUMN_POSITION = 3

    const val COUNT_NFC_COLUMN_NAME = "COUNT_NFC"
    const val COUNT_NFC_COLUMN_POSITION = 4

    const val COUNT_NFC_ANS_COLUMN_NAME = "COUNT_NFC_ANS"
    const val COUNT_NFC_ANS_COLUMN_POSITION = 5

    const val COUNT_QUESTION_COLUMN_NAME = "COUNT_QUESTION"
    const val COUNT_QUESTION_COLUMN_POSITION = 6

    const val COUNT_ANSWER_COLUMN_NAME = "COUNT_ANSWER"
    const val COUNT_ANSWER_COLUMN_POSITION = 7

    const val FACT_START_DATE_COLUMN_NAME = "FACT_START_DATE"
    const val FACT_START_DATE_COLUMN_POSITION = 8

    const val FACT_END_DATE_COLUMN_NAME = "FACT_END_DATE"
    const val FACT_END_DATE_COLUMN_POSITION = 9

    const val STATUS_CODE_COLUMN_NAME = "EAM_STATUS_CODE"

    fun getColumns(): List<String> {
        return listOf(
                ID_COLUMN_NAME,
                TASK_ID_COLUMN_NAME,
                EQUIPMENT_ID_COLUMN_NAME,
                ORDER_NUMBER_COLUMN_NAME,
                COUNT_NFC_COLUMN_NAME,
                COUNT_NFC_ANS_COLUMN_NAME,
                COUNT_QUESTION_COLUMN_NAME,
                COUNT_ANSWER_COLUMN_NAME,
                FACT_START_DATE_COLUMN_NAME,
                FACT_END_DATE_COLUMN_NAME
        )
    }
}
