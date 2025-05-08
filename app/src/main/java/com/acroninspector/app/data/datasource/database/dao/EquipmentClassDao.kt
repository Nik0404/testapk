package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.EquipmentClass
import io.reactivex.Flowable

@Dao
interface EquipmentClassDao {

    @Query("SELECT * FROM equipment_class")
    fun getAllEquipmentClasses(): Flowable<List<EquipmentClass>>
}
