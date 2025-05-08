package com.acroninspector.app.domain.entity.remote.request

import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.domain.entity.remote.request.other.Filter
import com.acroninspector.app.domain.entity.remote.request.other.Order
import com.google.gson.annotations.SerializedName

class GettingDataRequest(
    @SerializedName("sessionid") val sessionId: String,
    @SerializedName("functionid") val functionId: Int,
    @SerializedName("sboname") val functionName: String,
    @SerializedName("reqlist") val columns: List<String> = listOf(),
    @SerializedName("filter") val filter: List<Filter> = listOf(),
    @SerializedName("order") val order: List<Order> = listOf(),
    @SerializedName("maxrows") val maxrows: Int = NetworkConstants.SELECT_MAXROWS // Задание на доработку ИСА № 2854 от 02.09.2020г.
)
