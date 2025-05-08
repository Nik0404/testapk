package com.acroninspector.app.data.util.constants

object AnswerColumns {

    private const val ID_COLUMN_NAME = "QUERY_ID"
    const val ID_COLUMN_POSITION = 0

    const val CHECKLIST_ID_COLUMN_NAME = "CHECKLIST_ID"
    const val CHECKLIST_ID_COLUMN_POSITION = 1

    private const val ORDER_NUMBER_COLUMN_NAME = "ORDER_NUMBER"
    const val ORDER_NUMBER_COLUMN_POSITION = 2

    private const val ANSWER_TEXT_COLUMN_NAME = "ANSWER_VALUE"
    const val ANSWER_TEXT_COLUMN_POSITION = 3

    private const val IS_DEFECT_COLUMN_NAME = "DEFECT_FLAG"
    const val IS_DEFECT_COLUMN_POSITION = 4

    private const val IS_DEFAULT_COLUMN_NAME = "DEFAULT_FLAG"
    const val IS_DEFAULT_COLUMN_POSITION = 5

    fun getColumns(): List<String> {
        return listOf(
            ID_COLUMN_NAME,
            CHECKLIST_ID_COLUMN_NAME,
            ORDER_NUMBER_COLUMN_NAME,
            ANSWER_TEXT_COLUMN_NAME,
            IS_DEFECT_COLUMN_NAME,
            IS_DEFAULT_COLUMN_NAME
        )
    }
}