package com.acroninspector.app.common.constants

object NetworkConstants {

    const val APP_LOGIN_HOST_CODE = "ad4586ff3971f95a5c9489769ed3ca7d"
    const val APP_REGISTER_FUNCTION_CODE = "df54a0c93d94cf7616d1479bc68f29fd"

    // Задание на доработку ИСА № 2854 от 02.09.2020г.
    const val SELECT_MAXROWS = 25000
    // -----------------------------------------------
    const val TIME_OUT_MINUTES = 60L
    // -----------------------------------------------

    const val OUT_OF_DATE_PASSWORD_ERROR_CODE = -999982
    const val APP_VERSION_ERROR_CODE = -1017

    const val LESS_THAN_OR_EQUALS = "<="
    const val MORE_THAN_OR_EQUALS = ">="
    const val LESS_THAN = "<"
    const val MORE_THAN = ">"
    const val EQUALS = "="
    const val NOT_EQUALS = "<>"
    const val IN = "IN"

    const val ORDER_ASC = "ASC"
    const val ORDER_DESC = "DESC"

    const val STORAGE_ID = "storage.document_folder"
    const val LOGS_STORAGE_ID = "storage.logs_folder"

    const val DOC_TYPE_PHOTO = "Photo"
    const val DOC_TYPE_VIDEO = "Video"
    const val DOC_TYPE_AUDIO = "Audio"

    const val FILE_MAX_SIZE = 1024 * 1042 * 60
}
