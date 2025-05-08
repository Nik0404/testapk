package com.acroninspector.app.domain.entity.local.database

class Attachment(
        val attachmentId: Int,
        val checkListId: Int,
        val defectLogId: Int,
        val attachmentType: Int,
        val hashFile: String,
        val fileName: String
) : AcronEntity