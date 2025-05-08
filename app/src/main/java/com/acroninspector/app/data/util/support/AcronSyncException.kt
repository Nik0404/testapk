package com.acroninspector.app.data.util.support

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.FunctionNames

open class AcronSyncException(
        val functionName: String,
        cause: Throwable
) : RuntimeException(cause) {

    fun getEntityResId(): Int {
        return when (functionName) {
            FunctionNames.EXECUTORS -> R.string.executors_for_error_message
            FunctionNames.USER_GROUPS -> R.string.groups_for_error_message
            FunctionNames.DIRECTORIES -> R.string.directories_for_error_message
            FunctionNames.EQUIPMENT -> R.string.equipments_for_error_message
            FunctionNames.EQUIPMENT_CLASS -> R.string.equipment_classes_for_error_message
            FunctionNames.NFC_EQUIPMENT -> R.string.nfc_equipments_for_error_message
            FunctionNames.TASKS -> R.string.tasks_for_error_message
            FunctionNames.ROUTES -> R.string.routes_for_error_message
            FunctionNames.NFC_ROUTE -> R.string.nfc_routes_for_error_message
            FunctionNames.CHECK_LISTS -> R.string.check_lists_for_error_message
            FunctionNames.ANSWER -> R.string.answers_for_error_message
            FunctionNames.DEFECTS -> R.string.defects_for_error_message
            FunctionNames.DEFECT_LOG -> R.string.defect_logs_for_error_message
            FunctionNames.DEFECT_CAUSES -> R.string.defect_causes_for_error_message
            FunctionNames.DEFECT_RELATIONS -> R.string.defect_by_pos_for_error_message
            FunctionNames.ATTACHMENTS -> R.string.attachments_for_error_message
            else -> R.string.default_for_error_message
        }
    }
}