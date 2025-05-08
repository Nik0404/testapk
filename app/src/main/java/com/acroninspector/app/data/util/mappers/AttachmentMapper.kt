package com.acroninspector.app.data.util.mappers

import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.util.constants.AttachmentColumns
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Attachment

class AttachmentMapper : EntityMapper<List<String>, AcronEntity> {

    override fun fromSchemaToEntity(scheme: List<String>): AcronEntity {
        return Attachment(
                scheme[AttachmentColumns.ATTACHMENT_ID_COLUMN_POSITION].toId(),
                scheme[AttachmentColumns.CHECKLIST_ID_COLUMN_POSITION].toId(),
                scheme[AttachmentColumns.DEFECT_LOG_ID_COLUMN_POSITION].toId(),
                mapAttachmentType(scheme),
                scheme[AttachmentColumns.HASHFILE_COLUMN_POSITION],
                scheme[AttachmentColumns.FILE_NAME_COLUMN_POSITION]
        )
    }

    private fun mapAttachmentType(scheme: List<String>): Int {
        return when (val docType = scheme[AttachmentColumns.ATTACHMENT_TYPE_COLUMN_POSITION]) {
            NetworkConstants.DOC_TYPE_PHOTO -> Constants.MEDIA_TYPE_PHOTO
            NetworkConstants.DOC_TYPE_VIDEO -> Constants.MEDIA_TYPE_VIDEO
            NetworkConstants.DOC_TYPE_AUDIO -> Constants.MEDIA_TYPE_AUDIO
            else -> throw IllegalArgumentException("Unknown document type: $docType")
        }
    }
}