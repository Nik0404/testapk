package com.acroninspector.app.domain.entity.remote.response

import com.google.gson.annotations.SerializedName

class UserFullNameResponse(
    @SerializedName("Name") val name: String,
    @SerializedName("Surname") val surname: String,
    @SerializedName("Patroname") val thirdName: String,
    @SerializedName("UserDiv") val division: String,
    @SerializedName("Access") val access: Int
)
