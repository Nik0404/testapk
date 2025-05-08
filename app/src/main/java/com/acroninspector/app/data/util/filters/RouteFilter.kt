package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.RouteColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object RouteFilter {

    /**
     * @return filter that selected routes by task id
     */
    fun getFilterByTaskId(taskId: Int): List<Filter> {
        return listOf(
                Filter(
                        RouteColumns.TASK_ID_COLUMN_NAME,
                        NetworkConstants.EQUALS,
                        taskId.toString()
                )
        )
    }
}