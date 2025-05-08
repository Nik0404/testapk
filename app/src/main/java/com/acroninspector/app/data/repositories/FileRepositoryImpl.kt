package com.acroninspector.app.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.acroninspector.app.common.constants.Constants.AUDIO_EXTENSION_MP3
import com.acroninspector.app.common.constants.Constants.AUDIO_PREFIX
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_AUDIO
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_PHOTO
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_VIDEO
import com.acroninspector.app.common.constants.Constants.PHOTO_EXTENSION_JPG
import com.acroninspector.app.common.constants.Constants.PHOTO_PREFIX
import com.acroninspector.app.common.constants.Constants.VIDEO_EXTENSION_MP4
import com.acroninspector.app.common.constants.Constants.VIDEO_PREFIX
import com.acroninspector.app.domain.entity.local.other.AcronFile
import com.acroninspector.app.domain.repositories.FileRepository
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class FileRepositoryImpl(private val application: Context) : FileRepository {

    @SuppressLint("Recycle")
    override fun getAudioFilePathFromUri(uri: Uri): String {
        val cursor = application.contentResolver.query(uri,
                null, null, null, null)
        cursor?.moveToFirst()

        @Suppress("DEPRECATION")
        val index = cursor?.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)!!
        return cursor.getString(index)
    }

    override fun createFile(type: Int): AcronFile {
        var storageDir: File? = null
        var prefix: String? = null
        var suffix: String? = null

        when (type) {
            MEDIA_TYPE_PHOTO -> {
                storageDir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                prefix = PHOTO_PREFIX
                suffix = PHOTO_EXTENSION_JPG
            }
            MEDIA_TYPE_VIDEO -> {
                storageDir = application.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                prefix = VIDEO_PREFIX
                suffix = VIDEO_EXTENSION_MP4
            }
            MEDIA_TYPE_AUDIO -> {
                storageDir = application.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)
                prefix = AUDIO_PREFIX
                suffix = AUDIO_EXTENSION_MP3
            }
        }
        val mediaFile = File.createTempFile(prefix!!, suffix, storageDir)

        return createFile(mediaFile)
    }

    override fun createFile(fileName: String, content: ByteArray): AcronFile {
        val suffix: String = fileName.substring(fileName.length - 4, fileName.length)

        val storageDir: File?
        val prefix = when (suffix) {
            PHOTO_EXTENSION_JPG -> {
                storageDir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                PHOTO_PREFIX
            }
            VIDEO_EXTENSION_MP4 -> {
                storageDir = application.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                VIDEO_PREFIX
            }
            else -> {
                storageDir = application.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)
                AUDIO_PREFIX
            }
        }

        val mediaFile = File.createTempFile(prefix, suffix, storageDir)
        val bufferedOutputStream = BufferedOutputStream(FileOutputStream(mediaFile))
        bufferedOutputStream.write(content)
        bufferedOutputStream.flush()
        bufferedOutputStream.close()

        return createFile(mediaFile)
    }

    override fun createFile(file: File): AcronFile {
        val uri = FileProvider.getUriForFile(application,
                "com.acroninspector.app.provider", file)
        val filePath = file.absolutePath

        return AcronFile(uri, filePath)
    }
}