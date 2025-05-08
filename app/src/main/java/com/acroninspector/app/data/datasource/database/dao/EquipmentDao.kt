package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.Equipment
import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import com.acroninspector.app.domain.entity.local.display.DisplayDirectory
import com.acroninspector.app.domain.entity.local.display.DisplayEquipmentItem
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface EquipmentDao {

    @Query("SELECT * FROM equipment WHERE id = :equipmentId")
    fun getEquipmentById(equipmentId: Int): Single<Equipment>

    @Query("""
        SELECT id, directory_id AS directoryId, name, code, subdivision, building, additional_info AS additionalInfo,
         (SELECT short_name FROM equipment_class WHERE equipment_class.id = equipment.equipment_class_id) AS className,
         (SELECT count(*) FROM local_defect WHERE local_defect.equipment_id = equipment.id) AS defectsCount
        FROM equipment
    """)
    fun getAllEquipments(): Flowable<List<DisplayEquipmentItem>>

    @Query("""
        SELECT id, directory_id AS directoryId, name, code, subdivision, building, additional_info AS additionalInfo,
         (SELECT short_name FROM equipment_class WHERE equipment_class.id = equipment.equipment_class_id) AS className,
         (SELECT count(*) FROM local_defect WHERE local_defect.equipment_id = equipment.id) AS defectsCount
        FROM equipment
        WHERE directory_id = :directoryId
        ORDER BY equipment.code ASC
    """)
    fun getEquipmentsByDirectory(directoryId: Int): Flowable<List<DisplayEquipmentItem>>

    @Query("""
        SELECT id, directory_id AS directoryId, name, code, subdivision, building, additional_info AS additionalInfo,
         (SELECT short_name FROM equipment_class WHERE equipment_class.id = equipment.equipment_class_id) AS className,
         (SELECT count(*) FROM local_defect WHERE local_defect.equipment_id = equipment.id) AS defectsCount
        FROM equipment
        WHERE equipment.name LIKE '%' || :query || '%' 
            OR equipment.code LIKE '%' || :query || '%'
        ORDER BY equipment.code ASC
    """)
    fun getSearchedEquipments(query: String): Flowable<List<DisplayEquipmentItem>>

    @Query("SELECT * FROM nfc_equipment WHERE nfc_equipment.equipment_id = :equipmentId")
    fun getNfcMarksByEquipmentId(equipmentId: Int): List<NfcEquipment>

    @Query("""
        SELECT id, name, parent_id AS parentId, end_level AS endLvl
        FROM directory
        WHERE directory.id = :directoryId
    """)
    fun getDirectoryById(directoryId: Int): DisplayDirectory

    @Query("""
        SELECT id, directory_id AS directoryId, name, code, subdivision, building, additional_info AS additionalInfo,
         (SELECT short_name FROM equipment_class WHERE equipment_class.id = equipment.equipment_class_id) AS className,
         (SELECT count(*) FROM local_defect WHERE local_defect.equipment_id = equipment.id) AS defectsCount
        FROM equipment
        WHERE equipment.id = :equipmentId
    """)
    fun getDisplayEquipmentById(equipmentId: Int): Flowable<DisplayEquipmentItem>
}
