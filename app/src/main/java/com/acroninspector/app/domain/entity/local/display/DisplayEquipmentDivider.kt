package com.acroninspector.app.domain.entity.local.display

import com.acroninspector.app.common.constants.Constants

data class DisplayEquipmentDivider(val nameResourceId: Int) : DisplayEquipment {

    override fun getEquipmentType() = Constants.EQUIPMENT_DIVIDER
}