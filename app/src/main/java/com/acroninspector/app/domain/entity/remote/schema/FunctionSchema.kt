package com.acroninspector.app.domain.entity.remote.schema

import com.google.gson.annotations.SerializedName

class FunctionSchema(
    @SerializedName("FunctionCode") val functionCode: Int,
    @SerializedName("FunctionName") val functionName: String,
    @SerializedName("FunctionTitle") val functionTitle: String
)
