package com.acroninspector.app.domain.entity.remote.request

import com.google.gson.annotations.SerializedName

class LoginHostRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
    @SerializedName("applicationcode") val applicationCode: String,
    @SerializedName("applicationversion") val applicationVersion: String
)
