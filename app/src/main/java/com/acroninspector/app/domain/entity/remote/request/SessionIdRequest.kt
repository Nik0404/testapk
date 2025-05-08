package com.acroninspector.app.domain.entity.remote.request

import com.google.gson.annotations.SerializedName

data class SessionIdRequest(@SerializedName("sessionid") val sessionId: String)