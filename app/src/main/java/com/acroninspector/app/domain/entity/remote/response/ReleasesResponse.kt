package com.acroninspector.app.domain.entity.remote.response

import com.google.gson.annotations.SerializedName

data class ReleasesResponse(
    @SerializedName("ACTIVATION_TIME") val activationTime: String,
    @SerializedName("ACTIVATION_TIME_FNM") val activationTimeFNM: String,
    @SerializedName("RELEASE_ID") val releaseId: String
)