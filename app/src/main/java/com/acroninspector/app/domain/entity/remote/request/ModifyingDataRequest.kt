package com.acroninspector.app.domain.entity.remote.request

import com.acroninspector.app.domain.entity.remote.request.values.AcronValue
import com.google.gson.annotations.SerializedName

class ModifyingDataRequest(
        @SerializedName("sessionid") val sessionId: String,
        @SerializedName("functionid") val functionId: Int,
        @SerializedName("sboname") val functionName: String,
        @SerializedName("values") val values: AcronValue
)