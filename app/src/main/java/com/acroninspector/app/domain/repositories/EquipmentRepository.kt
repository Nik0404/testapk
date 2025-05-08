package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.Equipment
import com.acroninspector.app.domain.entity.local.display.DisplayEquipmentItem
import io.reactivex.Flowable
import io.reactivex.Single

interface EquipmentRepository {

    fun getAllEquipments(): Flowable<List<DisplayEquipmentItem>>

    fun getEquipmentsByDirectory(directoryId: Int): Flowable<List<DisplayEquipmentItem>>

    fun getEquipmentById(equipmentId: Int): Single<Equipment>

    fun getDisplayEquipmentById(equipmentId: Int): Flowable<DisplayEquipmentItem>

    fun getSearchedEquipments(query: String): Flowable<List<DisplayEquipmentItem>>
}
