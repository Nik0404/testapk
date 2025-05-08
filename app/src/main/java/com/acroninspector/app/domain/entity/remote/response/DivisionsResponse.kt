package com.acroninspector.app.domain.entity.remote.response

import com.acroninspector.app.domain.entity.remote.schema.DivisionSchema
import com.google.gson.annotations.SerializedName

class DivisionsResponse(
    @SerializedName("Divisions") val divisions: List<DivisionSchema>
)
