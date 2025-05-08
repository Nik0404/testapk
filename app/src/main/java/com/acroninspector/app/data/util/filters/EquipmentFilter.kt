package com.acroninspector.app.data.util.filters

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.util.constants.EquipmentColumns
import com.acroninspector.app.domain.entity.remote.request.other.Filter

object EquipmentFilter {

    /**
     * @return filter that selected equipments by division
     */
    fun getFilterByDivision(divisionId: Int): List<Filter> {
        return listOf(
                Filter(
                        EquipmentColumns.SUBDIVISION_COLUMN_NAME, NetworkConstants.EQUALS,
                        divisionId.toString()
                )
        )
    }
}