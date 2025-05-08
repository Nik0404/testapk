package com.acroninspector.app.domain.entity.remote.response

import com.google.gson.annotations.SerializedName

class DefectResponse(
    @SerializedName("DEFECT_UP") val defectLogId: Int
)