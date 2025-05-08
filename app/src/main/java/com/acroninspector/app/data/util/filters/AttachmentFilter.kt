package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.AttachmentColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object AttachmentFilter {

    /**
     * @return filter that selected attachments by checklist id
     */
    fun getFilterByCheckListId(checkListId: Int): List<Filter> {
        return listOf(
            Filter(
                AttachmentColumns.CHECKLIST_ID_COLUMN_NAME,
                NetworkConstants.EQUALS,
                checkListId.toString()
            )
        )
    }

    fun getFilterByCheckListIds(checkListId: String): List<Filter> {
        return listOf(
            Filter(
                AttachmentColumns.CHECKLIST_ID_COLUMN_NAME,
                NetworkConstants.IN,
                checkListId
            )
        )
    }

    /**
     * @return filter that selected attachments by checklist id
     */
    fun getFilterByDefectLogId(defectLogId: Int): List<Filter> {
        return listOf(
            Filter(
                AttachmentColumns.DEFECT_LOG_ID_COLUMN_NAME,
                NetworkConstants.EQUALS,
                defectLogId.toString()
                )
        )
    }
}