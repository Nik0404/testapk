package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.DefectLogColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object DefectLogFilter {

    /**
     * @return filter that selected defect logs by division
     */

    fun <T> filterFactory(value: T): List<Filter> {
        return when (value) {
            is Int -> {
                getFilterByIdList(value.toString())
            }

            is String -> {
                getFilterByIdList(value)
            }

            else -> {
                listOf()
            }
        }
    }

    fun getFilterByDivision(divisionId: Int): List<Filter> {
        return listOf(
            Filter(
                DefectLogColumns.SUBDIVISION_COLUMN_NAME, NetworkConstants.EQUALS,
                divisionId.toString()
            )
        )
    }

    private fun getFilterByIdList(idLIst: String): List<Filter> {
        return listOf(
            Filter(
                DefectLogColumns.EQUIPMENT_ID_COLUMN_NAME, NetworkConstants.IN,
                idLIst
            ),
            Filter(
                DefectLogColumns.DEFECT_IS_CORRECTED, NetworkConstants.EQUALS,
                "0"
            )
        )
    }
}