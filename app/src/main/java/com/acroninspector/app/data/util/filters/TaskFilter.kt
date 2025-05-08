package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.TaskColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object TaskFilter {

    /**
     * @return filter that selected new tasks
     */
    fun getFilterNewTasks(executorGroupIds: List<Int>, divisionId: Int): List<Filter> {
        return arrayListOf(
                Filter(
                        TaskColumns.STATUS_CODE_COLUMN_NAME, NetworkConstants.EQUALS,
                        DatabaseConstants.TASK_STATUS_NEW.toString()
                ),
                Filter(
                        TaskColumns.SUBDIVISION_ID_COLUMN_NAME, NetworkConstants.EQUALS,
                        divisionId.toString()
                ),
                Filter(
                        TaskColumns.EXECUTOR_GROUP_ID_COLUMN_NAME, NetworkConstants.IN,
                        executorGroupIds.joinToString()
                )
        )
    }

    /**
     * @return filter that selected new and in progress tasks
     */
    fun getFilterNotCompletedTasks(executorGroupIds: List<Int>, divisionId: Int): List<Filter> {
        return arrayListOf(
                Filter(
                        TaskColumns.STATUS_CODE_COLUMN_NAME, NetworkConstants.NOT_EQUALS,
                        DatabaseConstants.TASK_STATUS_COMPLETED.toString()
                ),
                Filter(
                        TaskColumns.SUBDIVISION_ID_COLUMN_NAME, NetworkConstants.EQUALS,
                        divisionId.toString()
                ),
                Filter(
                        TaskColumns.EXECUTOR_GROUP_ID_COLUMN_NAME, NetworkConstants.IN,
                        executorGroupIds.joinToString()
                )
        )
    }

    /**
     * @return filter that selected completed tasks
     */
    fun getFilterCompletedTasks(executorGroupIds: List<Int>, divisionId: Int): List<Filter> {
        return listOf(
                Filter(
                        TaskColumns.STATUS_CODE_COLUMN_NAME, NetworkConstants.EQUALS,
                        DatabaseConstants.TASK_STATUS_COMPLETED.toString()
                ),
                Filter(
                        TaskColumns.SUBDIVISION_ID_COLUMN_NAME, NetworkConstants.EQUALS,
                        divisionId.toString()
                ),
                Filter(
                        TaskColumns.EXECUTOR_GROUP_ID_COLUMN_NAME, NetworkConstants.IN,
                        executorGroupIds.joinToString()
                )
        )
    }
}