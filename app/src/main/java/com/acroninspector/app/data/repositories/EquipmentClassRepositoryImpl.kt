package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.EquipmentClassDao
import com.acroninspector.app.domain.entity.local.database.EquipmentClass
import com.acroninspector.app.domain.repositories.EquipmentClassRepository
import io.reactivex.Flowable

class EquipmentClassRepositoryImpl(
        private val equipmentClassDao: EquipmentClassDao
) : EquipmentClassRepository {

    override fun getAllEquipmentClasses(): Flowable<List<EquipmentClass>> {
        return equipmentClassDao.getAllEquipmentClasses()
    }
}