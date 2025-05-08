package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DefectCauseDao {

    @Query("""
        SELECT
            defect_cause.code AS id,
            defect_cause.short_name AS shortName,
            defect_cause.full_name AS fullName
        FROM defect_cause
            INNER JOIN defect_relation
                ON defect_relation.defect_cause_id = defect_cause.code
        WHERE defect_relation.equipment_class_id = :equipmentClassId
        AND defect_id = :defectNameId
    """)
    fun getAllDefectCauses(defectNameId: Int, equipmentClassId: Int): Flowable<List<DisplayDefectCause>>

    @Query("""
        SELECT
            defect_cause.code AS id,
            defect_cause.short_name AS shortName,
            defect_cause.full_name AS fullName
        FROM defect_cause
        WHERE defect_cause.code = :defectCauseId
    """)
    fun getDefectCauseById(defectCauseId: Int): Single<DisplayDefectCause>
}