package com.acroninspector.app.data.datasource.database.dao

import androidx.room.*
import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface NfcEquipmentDao {

    @Query("SELECT * FROM nfc_equipment WHERE nfc_equipment.equipment_id = :equipmentId")
    fun getNfcMarksByEquipmentId(equipmentId: Int): Flowable<List<NfcEquipment>>

    @Query("SELECT * FROM nfc_equipment")
    fun getNfcEquipmentMarks(): List<NfcEquipment>

    @Query("SELECT equipment_id FROM nfc_equipment WHERE code = :nfcCode")
    fun getEquipmentIdByNfcCode(nfcCode: String): Maybe<Int>

    @Query("SELECT MAX(id) FROM nfc_equipment")
    fun getMaxEquipmentNfcTagId(): Single<Int>

    @Query("SELECT * FROM nfc_equipment WHERE is_new = 1")
    fun getNfcEquipmentTagsForUploading(): Single<List<NfcEquipment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNfcMark(nfcEquipment: NfcEquipment): Completable

    @Delete
    fun deleteNfcEquipmentTag(nfcEquipment: NfcEquipment): Completable
}
