package com.acroninspector.app.domain.entity.remote.request

import com.google.gson.annotations.SerializedName

class LoginPinHostRequest(
    @SerializedName("cardid") val cardid: String,
    @SerializedName("pin") val pin: String,
    @SerializedName("applicationcode") val applicationCode: String,
    @SerializedName("applicationversion") val applicationVersion: String
)
