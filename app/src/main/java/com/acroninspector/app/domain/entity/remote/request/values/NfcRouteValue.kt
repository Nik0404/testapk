package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.NfcRouteColumns
import com.google.gson.annotations.SerializedName

data class NfcRouteValue(
        @SerializedName(NfcRouteColumns.ID_COLUMN_NAME) val nfcId: String,
        @SerializedName(NfcRouteColumns.ROUTE_ID_COLUMN_NAME) val routeId: String,
        @SerializedName(NfcRouteColumns.TIME_COLUMN_NAME) val nfcTime: String
) : AcronValue