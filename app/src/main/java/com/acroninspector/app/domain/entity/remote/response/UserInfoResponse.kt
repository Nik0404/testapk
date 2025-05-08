package com.acroninspector.app.domain.entity.remote.response

import com.acroninspector.app.domain.entity.remote.schema.DivisionSchema
import com.google.gson.annotations.SerializedName

class UserInfoResponse(
    @SerializedName("FIO") val userFullNameResponse: UserFullNameResponse,
    @SerializedName("defaultSupervisedDivision") val defaultSupervisedDivision: DivisionSchema
)
