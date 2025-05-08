package com.acroninspector.app.domain.entity.remote.request.other

import com.google.gson.annotations.SerializedName

class Order(
        @SerializedName("name") val name: String,
        @SerializedName("order") val order: String
)