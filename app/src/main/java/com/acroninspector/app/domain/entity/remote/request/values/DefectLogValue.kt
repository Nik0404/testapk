package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.DefectLogColumns
import com.google.gson.annotations.SerializedName

data class DefectLogValue(
        @SerializedName(DefectLogColumns.EQUIPMENT_ID_COLUMN_NAME) val positionId: String,
        @SerializedName(DefectLogColumns.DEFECT_DT_COLUMN_NAME) val dateCreation: String,
        @SerializedName(DefectLogColumns.DEFECT_CODE_COLUMN_NAME) val defectNameId: String,
        @SerializedName(DefectLogColumns.DEFECT_CAUSE_CODE_COLUMN_NAME) val defectCauseId: String,
        @SerializedName(DefectLogColumns.DEFECT_CRITICALITY_COLUMN_NAME) val criticality: String,
        @SerializedName(DefectLogColumns.CHECK_LIST_ID_COLUMN_NAME) val checkListId: String,
        @SerializedName(DefectLogColumns.DEFECT_NOTE_COLUMN_NAME) val comment: String
) : AcronValue