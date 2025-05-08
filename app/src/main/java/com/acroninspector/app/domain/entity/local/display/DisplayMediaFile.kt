package com.acroninspector.app.domain.entity.local.display

import androidx.room.Ignore

data class DisplayMediaFile(
        val id: Int,
        val mediaType: Int,
        val filePath: String,
        val uri: String
) {

    @Ignore
    var fileName: String = ""
}