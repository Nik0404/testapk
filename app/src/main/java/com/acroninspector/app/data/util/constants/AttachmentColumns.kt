package com.acroninspector.app.data.util.constants

object AttachmentColumns {

    const val ATTACHMENT_ID_COLUMN_NAME = "ATTACHMENT_ID"
    const val ATTACHMENT_ID_COLUMN_POSITION = 0

    const val CHECKLIST_ID_COLUMN_NAME = "CHECKLIST_ID"
    const val CHECKLIST_ID_COLUMN_POSITION = 1

    const val DEFECT_LOG_ID_COLUMN_NAME = "DEFECT_UP"
    const val DEFECT_LOG_ID_COLUMN_POSITION = 2

    const val ATTACHMENT_TYPE_COLUMN_NAME = "DOC_TYPE"
    const val ATTACHMENT_TYPE_COLUMN_POSITION = 3

    const val HASHFILE_COLUMN_NAME = "HASHFILE"
    const val HASHFILE_COLUMN_POSITION = 4

    const val FILE_NAME_COLUMN_NAME = "FILENAME"
    const val FILE_NAME_COLUMN_POSITION = 5

    fun getColumns(): List<String> {
        return listOf(
                ATTACHMENT_ID_COLUMN_NAME,
                CHECKLIST_ID_COLUMN_NAME,
                DEFECT_LOG_ID_COLUMN_NAME,
                ATTACHMENT_TYPE_COLUMN_NAME,
                HASHFILE_COLUMN_NAME,
                FILE_NAME_COLUMN_NAME
        )
    }
}