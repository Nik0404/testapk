package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.EquipmentClass
import io.reactivex.Flowable


interface EquipmentClassRepository {

    fun getAllEquipmentClasses(): Flowable<List<EquipmentClass>>
}
