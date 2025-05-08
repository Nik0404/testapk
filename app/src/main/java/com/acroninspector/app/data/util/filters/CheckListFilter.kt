package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.CheckListColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object CheckListFilter {

    /**
     * @return filter that selected checklists by route id
     */
    fun getFilterByRouteId(routeId: Int): List<Filter> {
        return listOf(
            Filter(
                CheckListColumns.ROUTE_ID_COLUMN_NAME,
                NetworkConstants.EQUALS,
                routeId.toString()
            )
        )
    }

    fun getFilterByRouteIdS(routeIds: String): List<Filter> {
        return listOf(
            Filter(
                CheckListColumns.ROUTE_ID_COLUMN_NAME,
                NetworkConstants.IN,
                routeIds
            )
        )
    }
}