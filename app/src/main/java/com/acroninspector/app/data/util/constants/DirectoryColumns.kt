package com.acroninspector.app.data.util.constants

object DirectoryColumns {

    private const val ID_COLUMN_NAME = "STRUCTURE_ID"
    const val ID_COLUMN_POSITION = 0

    private const val NAME_COLUMN_NAME = "NAME"
    const val NAME_COLUMN_POSITION = 1

    private const val SIGN_COLUMN_NAME = "SIGN"
    const val SIGN_COLUMN_POSITION = 2

    private const val PARENT_ID_COLUMN_NAME = "PARENT"
    const val PARENT_ID_COLUMN_POSITION = 3

    private const val END_LVL_COLUMN_NAME = "END_LVL"
    const val END_LVL_COLUMN_POSITION = 4

    const val SUBDIVISION_ID_COLUMN_NAME = "SUBDIVISION_ID"

    fun getColumns(): List<String> {
        return listOf(
                ID_COLUMN_NAME,
                NAME_COLUMN_NAME,
                SIGN_COLUMN_NAME,
                PARENT_ID_COLUMN_NAME,
                END_LVL_COLUMN_NAME
        )
    }
}
