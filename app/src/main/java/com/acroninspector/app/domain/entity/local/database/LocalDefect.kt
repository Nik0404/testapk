package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_defect")
data class LocalDefect(
        @ColumnInfo(name = "check_list_id") var checkListId: Int,
        @ColumnInfo(name = "equipment_id") var equipmentId: Int,
        @ColumnInfo(name = "task_id") var taskId: Int,
        @ColumnInfo(name = "defect_cause_id") var defectCauseId: Int,
        @ColumnInfo(name = "defect_id") var defectNameId: Int,
        @ColumnInfo(name = "comment") var comment: String,
        @ColumnInfo(name = "criticality") var criticality: Int,
        @ColumnInfo(name = "defect_date") var dateCreation: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_defect_id")
    var id: Int = 0
}