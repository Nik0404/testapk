package com.acroninspector.app.domain.entity.local.display

import com.acroninspector.app.common.constants.Constants

data class DisplayDirectory(
    val id: Int,
    val name: String,
    val parentId: Int,
    val endLvl: Boolean
) : DisplayEquipment {

    override fun getEquipmentType() = Constants.EQUIPMENT_FOLDER
}
