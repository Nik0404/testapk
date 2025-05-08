package com.acroninspector.app.data.util.constants

object ExecutorColumns {

    private const val EXECUTOR_ID_COLUMN_NAME = "EXECUTOR_ID"
    const val EXECUTOR_ID_COLUMN_POSITION = 0

    private const val EXECUTOR_FULL_NAME_COLUMN_NAME = "EXECUTOR_ID_FNM"
    const val EXECUTOR_FULL_NAME_COLUMN_POSITION = 1

    private const val EXECUTOR_SHORT_NAME_COLUMN_NAME = "EXECUTOR_ID_SNM"
    const val EXECUTOR_SHORT_NAME_COLUMN_POSITION = 2

    private const val EXECUTOR_GROUP_NUMBER_COLUMN_NAME = "EXECUTOR_GROUP"
    const val EXECUTOR_GROUP_NUMBER_COLUMN_POSITION = 3

    private const val EXECUTOR_GROUP_NAME_COLUMN_NAME = "EXECUTOR_GROUP_FNM"
    const val EXECUTOR_GROUP_NAME_COLUMN_POSITION = 4

    const val EXECUTOR_SUBDIVISION_ID_COLUMN_NAME = "SUBDIVISION"
    const val EXECUTOR_SUBDIVISION_ID_COLUMN_POSITION = 5

    fun getColumns(): List<String> {
        return listOf(
                EXECUTOR_ID_COLUMN_NAME,
                EXECUTOR_FULL_NAME_COLUMN_NAME,
                EXECUTOR_SHORT_NAME_COLUMN_NAME,
                EXECUTOR_GROUP_NUMBER_COLUMN_NAME,
                EXECUTOR_GROUP_NAME_COLUMN_NAME,
                EXECUTOR_SUBDIVISION_ID_COLUMN_NAME
        )
    }
}