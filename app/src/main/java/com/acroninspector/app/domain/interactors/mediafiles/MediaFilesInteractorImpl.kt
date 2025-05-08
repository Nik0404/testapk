package com.acroninspector.app.domain.interactors.mediafiles

import android.net.Uri
import com.acroninspector.app.common.constants.Constants.ENTITY_CHECK_LIST
import com.acroninspector.app.common.constants.Constants.ENTITY_DEFECT_LOG
import com.acroninspector.app.common.constants.Constants.ENTITY_ROUTE
import com.acroninspector.app.common.constants.Constants.ENTITY_TASK
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.Attachment
import com.acroninspector.app.domain.entity.local.database.MediaFile
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.acroninspector.app.domain.entity.local.other.AcronFile
import com.acroninspector.app.domain.repositories.*
import io.reactivex.*
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MediaFilesInteractorImpl(
        private val mediaFileRepository: MediaFileRepository,
        private val taskRepository: TaskRepository,
        private val routeRepository: RouteRepository,
        private val checkListRepository: CheckListRepository,
        private val localDefectRepository: LocalDefectRepository,
        private val fileRepository: FileRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : MediaFilesInteractor {

    override fun getMediaFilesByCreatedDefect(createdDefectId: Int): Flowable<List<DisplayMediaFile>> {
        return mediaFileRepository.getMediaFilesByDefectLogId(createdDefectId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getMediaFilesByEntityId(entityType: Int, entityId: Int): Flowable<List<DisplayMediaFile>> {
        return handleIfEntityExists(entityType, entityId)
                .flatMapPublisher { getMediaFilesByEntityIdFromDatabase(entityType, entityId) }
                .switchIfEmpty(getMediaFilesByEntityIdFromNetwork(entityType, entityId))
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    private fun handleIfEntityExists(entityType: Int, entityId: Int): Maybe<Int> {
        return when (entityType) {
            ENTITY_TASK -> taskRepository.checkIfTaskExists(entityId)
            ENTITY_ROUTE -> routeRepository.checkIfRouteExists(entityId)
            ENTITY_CHECK_LIST -> checkListRepository.checkIfCheckListExists(entityId)
            ENTITY_DEFECT_LOG -> localDefectRepository.checkIfDefectLogExists(entityId)
            else -> throw IllegalArgumentException("Unknown entity type: $entityType")
        }
    }

    private fun getMediaFilesByEntityIdFromDatabase(entityType: Int, entityId: Int): Flowable<List<DisplayMediaFile>> {
        return when (entityType) {
            ENTITY_TASK -> mediaFileRepository.getMediaFilesByTaskId(entityId)
            ENTITY_ROUTE -> mediaFileRepository.getMediaFilesByRouteId(entityId)
            ENTITY_CHECK_LIST -> mediaFileRepository.getMediaFilesByCheckListId(entityId)
            ENTITY_DEFECT_LOG -> mediaFileRepository.getMediaFilesByDefectLogId(entityId)
            else -> throw IllegalArgumentException("Unknown entity type: $entityType")
        }
    }

    private fun getMediaFilesByEntityIdFromNetwork(entityType: Int, entityId: Int): Flowable<List<DisplayMediaFile>> {
        val sessionId = preferencesRepository.functionSessionId
        val functionId = preferencesRepository.functionId

        return handleLoadingMediaFilesFromNetwork(sessionId, functionId, entityType, entityId)
                .toObservable()
                .flatMapIterable { it }
                .flatMapSingle { attachment ->
                    getMediaFilesContentFromNetwork(attachment.hashFile)
                            .map { responseBody ->
                                val acronFile = fileRepository
                                        .createFile(attachment.fileName, responseBody.bytes())

                                val mediaFile = DisplayMediaFile(
                                        attachment.attachmentId,
                                        attachment.attachmentType,
                                        acronFile.filePath,
                                        acronFile.uri.toString()
                                )
                                mediaFile.fileName = attachment.fileName
                                mediaFile
                            }
                }.toList().toFlowable()
                .onErrorResumeNext(Function { throwable ->
                    when (throwable) {
                        is UnknownHostException,
                        is ConnectException,
                        is SocketTimeoutException -> Flowable.just(arrayListOf())
                        else -> Flowable.error(throwable)
                    }
                })
    }

    private fun handleLoadingMediaFilesFromNetwork(sessionId: String, functionId: Int,
                                                   entityType: Int, entityId: Int): Single<List<Attachment>> {
        return when (entityType) {
            ENTITY_TASK -> mediaFileRepository.loadAttachmentsByTaskId(sessionId, functionId, entityId)
            ENTITY_ROUTE -> mediaFileRepository.loadAttachmentsByRouteId(sessionId, functionId, entityId)
            ENTITY_CHECK_LIST -> mediaFileRepository.loadAttachmentsByCheckListId(sessionId, functionId, entityId)
            ENTITY_DEFECT_LOG -> mediaFileRepository.loadAttachmentsByDefectLogId(sessionId, functionId, entityId)
            else -> throw IllegalArgumentException("Unknown entity type: $entityType")
        }
    }

    private fun getMediaFilesContentFromNetwork(hashFile: String): Single<ResponseBody> {
        val sessionId = preferencesRepository.functionSessionId
        val functionId = preferencesRepository.functionId

        return mediaFileRepository.loadAttachmentContentByHashFile(sessionId, functionId, hashFile)
    }

    override fun insertMediaFile(mediaFile: MediaFile, entityId: Int, entityType: Int): Completable {
        return handleInsertingMediaFile(mediaFile, entityType, entityId)
                .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun insertAudioFile(mediaType: Int, uri: Uri, entityId: Int, entityType: Int): Completable {
        return Observable.fromCallable { fileRepository.getAudioFilePathFromUri(uri) }
                .flatMapCompletable { filePath ->
                    val mediaFile = MediaFile(mediaType, filePath, uri.toString())
                    handleInsertingMediaFile(mediaFile, entityType, entityId)
                }.doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    private fun handleInsertingMediaFile(mediaFile: MediaFile, entityType: Int, entityId: Int): Completable {
        return when (entityType) {
            ENTITY_CHECK_LIST -> {
                mediaFile.checkListId = entityId
                mediaFileRepository.insertMediaFile(mediaFile)
            }
            ENTITY_DEFECT_LOG -> {
                mediaFile.defectLogId = entityId
                mediaFileRepository.insertMediaFile(mediaFile)
            }
            else -> Completable.error(IllegalArgumentException("Unknown entity type: $entityType"))
        }
    }

    override fun deleteMediaFile(mediaFileId: Int, entityId: Int, entityType: Int): Completable {
        return mediaFileRepository.deleteMediaFile(mediaFileId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun createFile(mediaType: Int): Single<AcronFile> {
        return Observable.fromCallable { fileRepository.createFile(mediaType) }
                .singleOrError()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}