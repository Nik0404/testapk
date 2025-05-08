package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.AttachmentColumns
import com.google.gson.annotations.SerializedName

class AttachmentValue(
        @SerializedName(AttachmentColumns.CHECKLIST_ID_COLUMN_NAME) val checkListId: String,
        @SerializedName(AttachmentColumns.DEFECT_LOG_ID_COLUMN_NAME) val defectLogId: String,
        @SerializedName(AttachmentColumns.ATTACHMENT_TYPE_COLUMN_NAME) val attachmentType: String,
        @SerializedName(AttachmentColumns.FILE_NAME_COLUMN_NAME) val fileName: String,
        @SerializedName(AttachmentColumns.HASHFILE_COLUMN_NAME) val hash: String
) : AcronValue