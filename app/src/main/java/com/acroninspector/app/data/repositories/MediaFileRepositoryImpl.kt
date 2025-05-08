package com.acroninspector.app.data.repositories

import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.common.extension.toId
import com.acroninspector.app.data.datasource.database.dao.MediaFileDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.util.constants.AttachmentColumns
import com.acroninspector.app.data.util.constants.CheckListColumns
import com.acroninspector.app.data.util.constants.RouteColumns
import com.acroninspector.app.data.util.filters.AttachmentFilter
import com.acroninspector.app.data.util.filters.CheckListFilter
import com.acroninspector.app.data.util.filters.RouteFilter
import com.acroninspector.app.data.util.mappers.AttachmentMapper
import com.acroninspector.app.domain.entity.local.database.Attachment
import com.acroninspector.app.domain.entity.local.database.MediaFile
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.acroninspector.app.domain.entity.remote.request.GettingDataRequest
import com.acroninspector.app.domain.entity.remote.request.other.Filter
import com.acroninspector.app.domain.repositories.MediaFileRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody

class MediaFileRepositoryImpl(
        private val functionsApi: FunctionsApi,
        private val mediaFileDao: MediaFileDao,
        private val attachmentMapper: AttachmentMapper
) : MediaFileRepository {

    override fun loadAttachmentsByTaskId(sessionId: String, functionId: Int,
                                         taskId: Int): Single<List<Attachment>> {
        return getEntityIds(
            sessionId = sessionId, functionId = functionId,
            functionName = FunctionNames.ROUTES,
            idColumnName = RouteColumns.ID_COLUMN_NAME,
            filter = RouteFilter.getFilterByTaskId(taskId)
        ).toObservable()
            .flatMapSingle { listId ->
                loadAttachmentsRouteId(sessionId, functionId, listId.joinToString(separator = ","))
            }

//            .flatMapIterable { it }
//            .flatMapSingle { routeId ->
//            //Load attachments for the each routeId of this task
//            loadAttachmentsByRouteId(sessionId, functionId, routeId)
            .combineAttachmentsMapper()
    }

    /**
    new way of image download
     */
    private fun loadAttachmentsRouteId(
        sessionId: String, functionId: Int,
        routeId: String
    ): Single<List<Attachment>> {
        return getEntityIds(
            sessionId = sessionId, functionId = functionId,
            functionName = FunctionNames.CHECK_LISTS,
            idColumnName = CheckListColumns.ID_COLUMN_NAME,
            filter = CheckListFilter.getFilterByRouteIdS(routeId)
        ).toObservable()
            .flatMapSingle { checkListId ->
                //Load attachments for the each checkListId of this route
                loadAttachmentsByCheckListIdS(
                    sessionId,
                    functionId,
                    checkListId.joinToString(separator = ",")
                )
            }.combineAttachmentsMapper()
    }

    private fun loadAttachmentsByCheckListIdS(
        sessionId: String, functionId: Int,
        checkListId: String
    ): Single<List<Attachment>> {
        return getAttachmentsByEntityId(
            sessionId = sessionId,
            functionId = functionId,
            filter = AttachmentFilter.getFilterByCheckListIds(checkListId)
        )
    }

    override fun loadAttachmentsByRouteId(
        sessionId: String, functionId: Int,
        routeId: Int
    ): Single<List<Attachment>> {
        return getEntityIds(
            sessionId = sessionId, functionId = functionId,
            functionName = FunctionNames.CHECK_LISTS,
            idColumnName = CheckListColumns.ID_COLUMN_NAME,
            filter = CheckListFilter.getFilterByRouteId(routeId)
        ).toObservable().flatMapIterable { it }.flatMapSingle { checkListId ->
            //Load attachments for the each checkListId of this route
            loadAttachmentsByCheckListId(sessionId, functionId, checkListId)
        }.combineAttachmentsMapper()
    }

    override fun loadAttachmentsByCheckListId(sessionId: String, functionId: Int,
                                              checkListId: Int): Single<List<Attachment>> {
        return getAttachmentsByEntityId(
                sessionId = sessionId,
                functionId = functionId,
                filter = AttachmentFilter.getFilterByCheckListId(checkListId)
        )
    }

    override fun loadAttachmentsByDefectLogId(sessionId: String, functionId: Int,
                                              defectLogId: Int): Single<List<Attachment>> {
        return getAttachmentsByEntityId(
                sessionId = sessionId,
                functionId = functionId,
                filter = AttachmentFilter.getFilterByDefectLogId(defectLogId)
        )
    }

    private fun Observable<List<Attachment>>.combineAttachmentsMapper(): Single<List<Attachment>> {
        return this.filter { it.isNotEmpty() }.toList().map { listOfAttachmentsLists ->
            //Combine all attachments into common list
            val attachments: MutableList<Attachment> = mutableListOf()
            listOfAttachmentsLists.forEach { checkListAttachments ->
                attachments.addAll(checkListAttachments)
            }
            attachments.toList()
        }
    }

    private fun getEntityIds(
            sessionId: String,
            functionId: Int,
            functionName: String,
            idColumnName: String,
            filter: List<Filter>
    ): Single<List<Int>> {
        val requestBody = GettingDataRequest(
                sessionId = sessionId, functionId = functionId,
                functionName = functionName,
                columns = arrayListOf(idColumnName),
                filter = filter
        )

        return functionsApi.getData(requestBody)
                .map { response ->
                    val ids = mutableListOf<Int>()
                    response.values.forEach { schema -> ids.add(schema[0].toId()) }
                    ids.toList()
                }
    }

    private fun getAttachmentsByEntityId(sessionId: String, functionId: Int,
                                         filter: List<Filter>): Single<List<Attachment>> {
        val requestBody = GettingDataRequest(
                sessionId = sessionId,
                functionId = functionId,
                functionName = FunctionNames.ATTACHMENTS,
                columns = AttachmentColumns.getColumns(),
                filter = filter
        )

        return functionsApi.getData(requestBody)
                .map { response ->
                    val attachments = mutableListOf<Attachment>()

                    response.values.forEach {
                        val attachmentItem = attachmentMapper.fromSchemaToEntity(it)
                        attachments.add(attachmentItem as Attachment)
                    }

                    attachments.toList()
                }
    }

    override fun loadAttachmentContentByHashFile(sessionId: String, functionId: Int,
                                                 hashFile: String): Single<ResponseBody> {
        return functionsApi.getAttachment(
                sessionId = sessionId,
                functionId = functionId,
                storageId = NetworkConstants.STORAGE_ID,
                fileId = hashFile
        )
    }

    override fun getMediaFilesByTaskId(taskId: Int): Flowable<List<DisplayMediaFile>> {
        return mediaFileDao.getMediaFilesByTaskId(taskId)
    }

    override fun getMediaFilesByRouteId(routeId: Int): Flowable<List<DisplayMediaFile>> {
        return mediaFileDao.getMediaFilesByRouteId(routeId)
    }

    override fun getMediaFilesByCheckListId(checkListId: Int): Flowable<List<DisplayMediaFile>> {
        return mediaFileDao.getMediaFilesByCheckListId(checkListId)
    }

    override fun getMediaFilesByDefectLogId(defectId: Int): Flowable<List<DisplayMediaFile>> {
        return mediaFileDao.getMediaFilesByDefectId(defectId)
    }

    override fun getMediaFileById(id: Int): Single<MediaFile> {
        return mediaFileDao.getMediaFileById(id)
    }

    override fun insertMediaFile(mediaFile: MediaFile): Completable {
        return mediaFileDao.insertMediaFile(mediaFile)
    }

    override fun deleteMediaFile(mediaFileId: Int): Completable {
        return mediaFileDao.deleteMediaFile(mediaFileId)
    }

    override fun deleteMediaFileByLocalDefectId(localDefectId: Int): Completable {
        return mediaFileDao.deleteMediaFileByLocalDefectId(localDefectId)
    }
}