package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.AnswerColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object AnswerFilter {

    /**
     * @return filter that selected answers by checklist id
     */
    fun getFilterByCheckListId(checkListId: Int): List<Filter> {
        return listOf(
                Filter(
                        AnswerColumns.CHECKLIST_ID_COLUMN_NAME,
                        NetworkConstants.EQUALS,
                        checkListId.toString()
                )
        )
    }
}