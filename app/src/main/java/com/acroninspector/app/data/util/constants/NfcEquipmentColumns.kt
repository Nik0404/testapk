package com.acroninspector.app.data.util.constants

object NfcEquipmentColumns {

    private const val ID_COLUMN_NAME = "NFC_ID"
    const val ID_COLUMN_POSITION = 0

    const val EQUIPMENT_ID_COLUMN_NAME = "POSITION_ID"
    const val EQUIPMENT_ID_COLUMN_POSITION = 1

    const val NAME_COLUMN_NAME = "NFC_NAME"
    const val NAME_COLUMN_POSITION = 2

    const val CODE_COLUMN_NAME = "NFC_CODE"
    const val CODE_COLUMN_POSITION = 3

    fun getColumns(): List<String> {
        return listOf(
                ID_COLUMN_NAME,
                EQUIPMENT_ID_COLUMN_NAME,
                NAME_COLUMN_NAME,
                CODE_COLUMN_NAME
        )
    }
}
