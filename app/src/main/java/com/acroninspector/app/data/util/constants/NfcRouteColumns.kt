package com.acroninspector.app.data.util.constants

object NfcRouteColumns {

    const val ID_COLUMN_NAME = "NFC_ID"
    const val ID_COLUMN_POSITION = 0

    const val ROUTE_ID_COLUMN_NAME = "ROUTE_OBJECT_ID"
    const val ROUTE_ID_COLUMN_POSITION = 1

    private const val NAME_COLUMN_NAME = "NFC_NAME"
    const val NAME_COLUMN_POSITION = 2

    private const val CODE_COLUMN_NAME = "NFC_CODE"
    const val CODE_COLUMN_POSITION = 3

    const val TIME_COLUMN_NAME = "NFC_TIME"
    const val TIME_COLUMN_POSITION = 4

    fun getColumns(): List<String> {
        return listOf(
                ID_COLUMN_NAME,
                ROUTE_ID_COLUMN_NAME,
                NAME_COLUMN_NAME,
                CODE_COLUMN_NAME,
                TIME_COLUMN_NAME
        )
    }
}
