package com.acroninspector.app.domain.entity.local.display

import androidx.room.Ignore

class DisplayControlProcedure(
    val id: Int,
    val equipmentName: String,
    val equipmentCode: String,
    var orderNumber: Int
) {

    @Ignore
    var isCorrectValue = true
}