package com.acroninspector.app.domain.entity.remote.request

import com.google.gson.annotations.SerializedName

class GettingDivisionsRequest(
    @SerializedName("sessionid") val sessionId: String,
    @SerializedName("functionid") val functionId: Int
)
