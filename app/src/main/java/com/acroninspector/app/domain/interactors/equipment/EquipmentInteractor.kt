package com.acroninspector.app.domain.interactors.equipment

import com.acroninspector.app.domain.entity.local.display.DisplayEquipment
import com.acroninspector.app.domain.entity.local.display.DisplayEquipmentItem
import io.reactivex.Flowable

interface EquipmentInteractor {

    fun getFunctionId(): Int

    fun getCombinedEquipmentsAndDirectories(parentId: Int): Flowable<List<DisplayEquipment>>

    fun getSearchedEquipmentsAndDirectories(searchQuery: String): Flowable<List<DisplayEquipment>>

    fun getEquipmentById(equipmentId: Int): Flowable<DisplayEquipmentItem>
}
