package com.acroninspector.app.domain.entity.remote.request

import com.google.gson.annotations.SerializedName

class RegisterFunctionRequest(
    @SerializedName("sessionid")
    val sessionId: String,
    @SerializedName("applicationcode")
    val applicationCode: String,
    @SerializedName("applicationversion")
    val applicationVersion: String,
    @SerializedName("functionid")
    val functionId: Int
)
