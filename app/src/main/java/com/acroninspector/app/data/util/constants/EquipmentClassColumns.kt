package com.acroninspector.app.data.util.constants

object EquipmentClassColumns {

    private const val ID_COLUMN_NAME = "CODE"
    const val ID_COLUMN_POSITION = 0

    private const val SHORT_NAME_COLUMN_NAME = "SHORT_NAME"
    const val SHORT_NAME_COLUMN_POSITION = 1

    private const val FULL_NAME_COLUMN_NAME = "FULL_NAME"
    const val FULL_NAME_COLUMN_POSITION = 2

    fun getColumns(): List<String> {
        return listOf(
                ID_COLUMN_NAME,
                SHORT_NAME_COLUMN_NAME,
                FULL_NAME_COLUMN_NAME
        )
    }
}
