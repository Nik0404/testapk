package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DefectDao {

    @Query("""
        SELECT
            defect.code AS id,
            defect.short_name AS shortName,
            defect.full_name AS fullName
        FROM defect
            INNER JOIN defect_relation
                ON defect_relation.defect_id = defect.code
            WHERE defect_relation.equipment_class_id = :equipmentClassId
    """)
    fun getAllDefects(equipmentClassId: Int): Flowable<List<DisplayDefect>>

    @Query("""
        SELECT
            defect.code AS id,
            defect.short_name AS shortName,
            defect.full_name AS fullName
        FROM defect
            INNER JOIN defect_relation
                ON defect_relation.defect_id = defect.code
            WHERE defect.full_name LIKE '%' || :query || '%'
            AND defect_relation.equipment_class_id = :equipmentClassId
    """)
    fun getSearchedDefects(query: String, equipmentClassId: Int): Flowable<List<DisplayDefect>>

    @Query("""
        SELECT
            defect.code AS id,
            defect.short_name AS shortName,
            defect.full_name AS fullName
        FROM defect
        WHERE defect.code = :defectId
    """)
    fun getDefectById(defectId: Int): Single<DisplayDefect>
}