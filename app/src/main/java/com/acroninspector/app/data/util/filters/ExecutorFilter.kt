package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.ExecutorColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object ExecutorFilter {

    /**
     * @return filter that selected executors by division
     */
    fun getFilterByDivision(divisionId: Int): List<Filter> {
        return listOf(
                Filter(
                        ExecutorColumns.EXECUTOR_SUBDIVISION_ID_COLUMN_NAME, NetworkConstants.EQUALS,
                        divisionId.toString()
                )
        )
    }
}