package com.acroninspector.app.data.util.constants

object EquipmentColumns {

    private const val ID_COLUMN_NAME = "POSITION_ID"
    const val ID_COLUMN_POSITION = 0

    private const val DIRECTORY_ID_COLUMN_NAME = "STRUCTURE_ID"
    const val DIRECTORY_ID_COLUMN_POSITION = 1

    private const val EQUIPMENT_CLASS_ID_COLUMN_NAME = "CLASS_CODE"
    const val EQUIPMENT_CLASS_ID_COLUMN_POSITION = 2

    private const val NAME_COLUMN_NAME = "POSITION_NAME"
    const val NAME_COLUMN_POSITION = 3

    private const val POSITION_CODE_COLUMN_NAME = "POSITION_SCHEME_SIGN"
    const val POSITION_CODE_COLUMN_POSITION = 4

    const val SUBDIVISION_COLUMN_NAME = "SUBDIVISION"
    const val SUBDIVISION_COLUMN_POSITION = 5

    private const val BUILDING_COLUMN_NAME = "BUILDING"
    const val BUILDING_COLUMN_POSITION = 6

    private const val ADDITIONAL_INFO_COLUMN_NAME = "ADD_INFO"
    const val ADDITIONAL_INFO_COLUMN_POSITION = 7

    fun getColumns(): List<String> {
        return listOf(
                ID_COLUMN_NAME,
                DIRECTORY_ID_COLUMN_NAME,
                EQUIPMENT_CLASS_ID_COLUMN_NAME,
                NAME_COLUMN_NAME,
                POSITION_CODE_COLUMN_NAME,
                SUBDIVISION_COLUMN_NAME,
                BUILDING_COLUMN_NAME,
                ADDITIONAL_INFO_COLUMN_NAME
        )
    }
}
