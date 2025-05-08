package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.Attachment
import com.acroninspector.app.domain.entity.local.database.MediaFile
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.ResponseBody

interface MediaFileRepository {

    fun loadAttachmentsByTaskId(
            sessionId: String,
            functionId: Int,
            taskId: Int
    ): Single<List<Attachment>>

    fun loadAttachmentsByRouteId(
            sessionId: String,
            functionId: Int,
            routeId: Int
    ): Single<List<Attachment>>

    fun loadAttachmentsByCheckListId(
            sessionId: String,
            functionId: Int,
            checkListId: Int
    ): Single<List<Attachment>>

    fun loadAttachmentsByDefectLogId(
            sessionId: String,
            functionId: Int,
            defectLogId: Int
    ): Single<List<Attachment>>

    fun loadAttachmentContentByHashFile(
            sessionId: String,
            functionId: Int,
            hashFile: String
    ): Single<ResponseBody>

    fun getMediaFilesByTaskId(taskId: Int): Flowable<List<DisplayMediaFile>>

    fun getMediaFilesByRouteId(routeId: Int): Flowable<List<DisplayMediaFile>>

    fun getMediaFilesByCheckListId(checkListId: Int): Flowable<List<DisplayMediaFile>>

    fun getMediaFilesByDefectLogId(defectId: Int): Flowable<List<DisplayMediaFile>>

    fun getMediaFileById(id: Int): Single<MediaFile>

    fun insertMediaFile(mediaFile: MediaFile): Completable

    fun deleteMediaFile(mediaFileId: Int): Completable

    fun deleteMediaFileByLocalDefectId(localDefectId: Int): Completable
}