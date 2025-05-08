package com.acroninspector.app.domain.entity.local.display

import com.acroninspector.app.common.constants.Constants

data class DisplayEquipmentSpace(val spaceResourceId: Int) : DisplayEquipment {

    override fun getEquipmentType() = Constants.EQUIPMENT_SPACE
}