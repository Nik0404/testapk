package com.acroninspector.app.domain.entity.local.other

import androidx.room.Ignore

class MediaFileWithHash(
        val id: Int,
        val checkListId: Int,
        val defectLogId: Int,
        val mediaType: Int,
        val filePath: String
) {

    @Ignore
    var hash: String = ""
}