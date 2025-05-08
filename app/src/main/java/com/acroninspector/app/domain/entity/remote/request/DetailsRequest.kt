package com.acroninspector.app.domain.entity.remote.request

import com.google.gson.annotations.SerializedName

class DetailsRequest(
    @SerializedName("sessionid")
    val sessionId: String,
    @SerializedName("releaseid")
    val releaseid: String,
    @SerializedName("applicationcode")
    val applicationCode: String,
)