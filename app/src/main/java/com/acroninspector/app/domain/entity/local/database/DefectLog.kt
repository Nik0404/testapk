package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog

@Entity(tableName = "defect_log")
data class DefectLog(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "equipment_id") val equipmentId: Int,
    @ColumnInfo(name = "equipment_name") val equipmentName: String,
    @ColumnInfo(name = "equipment_code") val equipmentCode: String,
    @ColumnInfo(name = "defect_name") val defectName: String,
    @ColumnInfo(name = "defect_cause_name") val defectCauseName: String,
    @ColumnInfo(name = "defect_date") val dateCreation: String,
    @ColumnInfo(name = "defect_criticality") val criticality: Int,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "is_corrected") val isCorrected: Int,
) : AcronEntity {

    fun toDisplayDefectLog() = DisplayDefectLog(
        id = id,
        equipmentName = equipmentName,
        equipmentCode = equipmentCode,
        timestamp = dateCreation,
        criticality = criticality,
        comment = comment
    ).apply {
        this.defectName = this@DefectLog.defectName
        this.defectCauseName = this@DefectLog.defectCauseName

    }
}
