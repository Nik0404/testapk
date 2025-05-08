package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "defect_relation", primaryKeys = ["equipment_class_id", "defect_id", "defect_cause_id"])
data class DefectRelation(
        @ColumnInfo(name = "equipment_class_id") val equipmentClassId: Int,
        @ColumnInfo(name = "defect_id") val defectId: Int,
        @ColumnInfo(name = "defect_cause_id") val defectCauseId: Int
) : AcronEntity