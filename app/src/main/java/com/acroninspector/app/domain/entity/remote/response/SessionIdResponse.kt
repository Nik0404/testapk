package com.acroninspector.app.domain.entity.remote.response

import com.google.gson.annotations.SerializedName

class SessionIdResponse(
    @SerializedName("sessionid") val sessionId: String
)
