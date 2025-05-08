package com.acroninspector.app.domain.interactors.mediafiles

import android.net.Uri
import com.acroninspector.app.domain.entity.local.database.MediaFile
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.acroninspector.app.domain.entity.local.other.AcronFile
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface MediaFilesInteractor {

    fun getMediaFilesByEntityId(entityType: Int, entityId: Int): Flowable<List<DisplayMediaFile>>

    fun getMediaFilesByCreatedDefect(createdDefectId: Int): Flowable<List<DisplayMediaFile>>

    fun insertMediaFile(mediaFile: MediaFile, entityId: Int, entityType: Int): Completable

    fun insertAudioFile(mediaType: Int, uri: Uri, entityId: Int, entityType: Int): Completable

    fun deleteMediaFile(mediaFileId: Int, entityId: Int, entityType: Int): Completable

    fun createFile(mediaType: Int): Single<AcronFile>
}