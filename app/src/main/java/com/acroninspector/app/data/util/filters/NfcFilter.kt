package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.NfcRouteColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object NfcFilter {

    /**
     * @return filter that selected nfc marks by route id
     */
    fun getFilterByRouteId(routeId: Int): List<Filter> {
        return listOf(
                Filter(
                        NfcRouteColumns.ROUTE_ID_COLUMN_NAME,
                        NetworkConstants.EQUALS,
                        routeId.toString()
                )
        )
    }
}