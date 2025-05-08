package com.acroninspector.app.data.util.constants

object DefectRelationColumns {

    private const val CLASS_CODE_COLUMN_NAME = "CLASS_CODE"
    const val CLASS_CODE_COLUMN_POSITION = 0

    private const val DEFECT_ID_COLUMN_NAME = "DEFECT_CODE"
    const val DEFECT_ID_COLUMN_POSITION = 1

    private const val DEFECT_CAUSE_ID_COLUMN_NAME = "DEFECT_CAUSE_CODE"
    const val DEFECT_CAUSE_ID_COLUMN_POSITION = 2

    fun getColumns(): List<String> {
        return listOf(
                CLASS_CODE_COLUMN_NAME,
                DEFECT_ID_COLUMN_NAME,
                DEFECT_CAUSE_ID_COLUMN_NAME
        )
    }
}