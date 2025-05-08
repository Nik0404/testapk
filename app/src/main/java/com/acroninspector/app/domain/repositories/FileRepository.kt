package com.acroninspector.app.domain.repositories

import android.net.Uri
import com.acroninspector.app.domain.entity.local.other.AcronFile
import java.io.File

interface FileRepository {

    fun getAudioFilePathFromUri(uri: Uri): String

    fun createFile(type: Int): AcronFile

    fun createFile(fileName: String, content: ByteArray): AcronFile

    fun createFile(file: File): AcronFile
}