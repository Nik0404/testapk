package com.acroninspector.app.domain.entity.remote.request

import com.google.gson.annotations.SerializedName

class RegisterReleasesRequest(
    @SerializedName("sessionid")
    val sessionId: String,
    @SerializedName("appcode")
    val applicationCode: String
)