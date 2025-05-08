package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.CheckListColumns
import com.google.gson.annotations.SerializedName

data class CheckListValue(
        @SerializedName(CheckListColumns.ID_COLUMN_NAME) val id: String,
        @SerializedName(CheckListColumns.HAS_DEFECT_COLUMN_NAME) val defectFlag: String,
        @SerializedName(CheckListColumns.COUNT_ATTACHMENTS_COLUMN_NAME) val attachmentsCount: String,
        @SerializedName(CheckListColumns.USER_COMMENT_COLUMN_NAME) val comment: String,
        @SerializedName(CheckListColumns.FACT_END_DATE_COLUMN_NAME) val factEndDate: String
) : AcronValue {

    @SerializedName(CheckListColumns.VALUE_COLUMN_NAME) var answer: String = ""
    @SerializedName(CheckListColumns.VALUE_NUM_COLUMN_NAME) var answerNumber: String = ""
}