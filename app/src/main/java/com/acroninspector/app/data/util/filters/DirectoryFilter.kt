package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.DirectoryColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object DirectoryFilter {

    /**
     * @return filter that selected directories by division
     */
    fun getFilterByDivision(divisionId: Int): List<Filter> {
        return listOf(
                Filter(
                        DirectoryColumns.SUBDIVISION_ID_COLUMN_NAME, NetworkConstants.EQUALS,
                        divisionId.toString()
                )
        )
    }
}