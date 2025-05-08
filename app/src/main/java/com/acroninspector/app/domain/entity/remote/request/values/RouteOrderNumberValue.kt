package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.RouteColumns
import com.google.gson.annotations.SerializedName

class RouteOrderNumberValue(
        @SerializedName(RouteColumns.ID_COLUMN_NAME) val routeId: String,
        @SerializedName(RouteColumns.ORDER_NUMBER_COLUMN_NAME) val orderNumber: String
) : AcronValue