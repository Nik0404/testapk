package com.acroninspector.app.domain.entity.remote.request

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
        @SerializedName("sessionid")
        val sessionId: String,
        @SerializedName("applicationcode")
        val applicationCode: String
)