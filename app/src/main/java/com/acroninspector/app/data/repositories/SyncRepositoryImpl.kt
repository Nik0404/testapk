package com.acroninspector.app.data.repositories

import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.constants.FunctionNames.ANSWER
import com.acroninspector.app.common.constants.FunctionNames.ATTACHMENTS
import com.acroninspector.app.common.constants.FunctionNames.CHECK_LISTS
import com.acroninspector.app.common.constants.FunctionNames.DEFECTS
import com.acroninspector.app.common.constants.FunctionNames.DEFECT_CAUSES
import com.acroninspector.app.common.constants.FunctionNames.DEFECT_LOG
import com.acroninspector.app.common.constants.FunctionNames.DEFECT_RELATIONS
import com.acroninspector.app.common.constants.FunctionNames.DIRECTORIES
import com.acroninspector.app.common.constants.FunctionNames.EQUIPMENT
import com.acroninspector.app.common.constants.FunctionNames.EQUIPMENT_CLASS
import com.acroninspector.app.common.constants.FunctionNames.EXECUTORS
import com.acroninspector.app.common.constants.FunctionNames.NFC_EQUIPMENT
import com.acroninspector.app.common.constants.FunctionNames.NFC_ROUTE
import com.acroninspector.app.common.constants.FunctionNames.NOTIFICATIONS
import com.acroninspector.app.common.constants.FunctionNames.ROUTES
import com.acroninspector.app.common.constants.FunctionNames.TASKS
import com.acroninspector.app.common.constants.FunctionNames.USER_GROUPS
import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.common.extension.deleteSpaces
import com.acroninspector.app.common.extension.filePathToFileName
import com.acroninspector.app.common.extension.toAcronId
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.datasource.network.SessionApi
import com.acroninspector.app.data.datasource.network.UserApi
import com.acroninspector.app.data.util.constants.AnswerColumns
import com.acroninspector.app.data.util.constants.CheckListColumns
import com.acroninspector.app.data.util.constants.NfcRouteColumns
import com.acroninspector.app.data.util.constants.RouteColumns
import com.acroninspector.app.data.util.constants.TaskColumns
import com.acroninspector.app.data.util.filters.AnswerFilter
import com.acroninspector.app.data.util.filters.CheckListFilter
import com.acroninspector.app.data.util.filters.NfcFilter
import com.acroninspector.app.data.util.filters.RouteFilter
import com.acroninspector.app.data.util.filters.TaskFilter
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.data.util.mappers.base.MapperFactory
import com.acroninspector.app.data.util.support.AcronDataProcessingException
import com.acroninspector.app.data.util.support.AcronNoInternetException
import com.acroninspector.app.domain.entity.local.database.AcronEntity
import com.acroninspector.app.domain.entity.local.database.Answer
import com.acroninspector.app.domain.entity.local.database.CheckList
import com.acroninspector.app.domain.entity.local.database.Defect
import com.acroninspector.app.domain.entity.local.database.DefectCause
import com.acroninspector.app.domain.entity.local.database.DefectLog
import com.acroninspector.app.domain.entity.local.database.DefectRelation
import com.acroninspector.app.domain.entity.local.database.Directory
import com.acroninspector.app.domain.entity.local.database.Equipment
import com.acroninspector.app.domain.entity.local.database.EquipmentClass
import com.acroninspector.app.domain.entity.local.database.Executor
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.database.MediaFile
import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import com.acroninspector.app.domain.entity.local.database.NfcRoute
import com.acroninspector.app.domain.entity.local.database.Notification
import com.acroninspector.app.domain.entity.local.database.Route
import com.acroninspector.app.domain.entity.local.database.Task
import com.acroninspector.app.domain.entity.local.database.UserGroup
import com.acroninspector.app.domain.entity.local.other.MediaFileWithHash
import com.acroninspector.app.domain.entity.remote.request.GettingDataRequest
import com.acroninspector.app.domain.entity.remote.request.ModifyingDataRequest
import com.acroninspector.app.domain.entity.remote.request.other.Filter
import com.acroninspector.app.domain.entity.remote.request.values.AttachmentValue
import com.acroninspector.app.domain.entity.remote.request.values.CheckListValue
import com.acroninspector.app.domain.entity.remote.request.values.DataLogStorage
import com.acroninspector.app.domain.entity.remote.request.values.DefectLogValue
import com.acroninspector.app.domain.entity.remote.request.values.NfcEquipmentValue
import com.acroninspector.app.domain.entity.remote.request.values.NfcRouteValue
import com.acroninspector.app.domain.entity.remote.request.values.RouteOrderNumberValue
import com.acroninspector.app.domain.entity.remote.request.values.RouteValue
import com.acroninspector.app.domain.entity.remote.request.values.TaskExecutorValue
import com.acroninspector.app.domain.entity.remote.request.values.TaskStatusValue
import com.acroninspector.app.domain.entity.remote.request.values.TaskValue
import com.acroninspector.app.domain.entity.remote.response.SelectResponse
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SyncRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Calendar
import javax.net.ssl.SSLException
import kotlin.math.min


class SyncRepositoryImpl(
    appDatabase: AppDatabase,
    private val userApi: UserApi,
    private val sessionApi: SessionApi,
    private val functionsApi: FunctionsApi,
    private val mapperFactory: MapperFactory,
    private val preferencesRepository: PreferencesRepository
) : SyncRepository {

    private val syncDao = appDatabase.syncDao()
    private val localDefectDao = appDatabase.localDefectDao()
    private val taskDao = appDatabase.taskDao()
    private val notificationDao = appDatabase.notificationsDao()
    private val routeDao = appDatabase.routeDao()
    private val checkListDao = appDatabase.checkListDao()
    private val nfcEquipmentDao = appDatabase.nfcEquipmentDao()
    private val nfcRouteDao = appDatabase.nfcRouteDao()
    private val mediaFileDao = appDatabase.mediaFileDao()

    var functionId: Int = 0
    var sessionId: String = ""

    override fun setApiIds(functionId: Int, sessionId: String) {
        this.functionId = functionId
        this.sessionId = sessionId

        DataLogStorage.login = preferencesRepository.login
        DataLogStorage.deviceId = preferencesRepository.deviceId
        DataLogStorage.date = DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)
    }

    private fun Completable.catchAcronException(functionName: String): Completable {
        return this.onErrorResumeNext { throwable ->
            val exception = when (throwable) {
                is SocketTimeoutException,
                is SSLException,
                is ConnectException,
                is UnknownHostException -> AcronNoInternetException(functionName, throwable)

                else -> AcronDataProcessingException(functionName, throwable)
            }
            DataLogStorage.error = exception.message.toString()
            Completable.error(exception)
        }
    }

    override fun loadAcronEntities(
        functionName: String,
        columns: List<String>,
        filter: List<Filter>
    ): Completable {
        val requestBody = GettingDataRequest(sessionId, functionId, functionName, columns, filter)
        val mapper = mapperFactory.createMapperByFunctionName(functionName)

        return handleApi(functionName, requestBody)
            .map { response ->
                val values = mutableListOf<AcronEntity>()

                response.values.forEach {
                    val entity = mapper.fromSchemaToEntity(it)
                    values.add(entity)
                }

                values.toList()
            }.flatMapCompletable { handleSaving(functionName, it) }
            .catchAcronException(functionName)
    }

    override fun loadTasks(functionId: Int): Completable {
        return Observable.just(true)
            .flatMapCompletable {
                val executorGroupIds = preferencesRepository.executorGroupIds
                val divisionid = preferencesRepository.supervisedDivisionId

                val filter = if (functionId == Functions.BYPASS) {
                    TaskFilter.getFilterNotCompletedTasks(executorGroupIds, divisionid)
                } else TaskFilter.getFilterNewTasks(executorGroupIds, divisionid)

                val columns = TaskColumns.getColumns()
                val requestBody = GettingDataRequest(sessionId, functionId, TASKS, columns, filter)
                val mapper = mapperFactory.createMapperByFunctionName(TASKS)

                handleApi(TASKS, requestBody)
                    .map { response ->
                        createNotifications(response, mapper)
                    }.flatMapCompletable { tasksAndNotifications ->
                        Completable.mergeArray(
                            handleSaving(TASKS, tasksAndNotifications.first),
                            handleSaving(NOTIFICATIONS, tasksAndNotifications.second)
                        )
                    }
            }.catchAcronException(TASKS)
    }

    private fun createNotifications(
        response: SelectResponse,
        mapper: EntityMapper<List<String>, AcronEntity>
    ): Pair<List<AcronEntity>, List<AcronEntity>> {
        val tasks = mutableListOf<AcronEntity>()
        val notifications = mutableListOf<AcronEntity>()

        response.values.forEach {
            val task = mapper.fromSchemaToEntity(it) as Task
            val dateCreation =
                DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)
            val notification = Notification(task.id, dateCreation, task.notificationDate)

            if (task.taskStatusCode != DatabaseConstants.TASK_STATUS_IN_PROGRESS ||
                task.executorId == preferencesRepository.executorId
            ) {
                tasks.add(task)
                notifications.add(notification)
            }
        }

        return Pair(tasks.toList(), notifications.toList())
    }

    override fun loadRoutes(): Completable {
        return syncDao.getTasks()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable {
                val filter = RouteFilter.getFilterByTaskId(it.id)
                loadAcronEntities(ROUTES, RouteColumns.getColumns(), filter)
            }.catchAcronException(ROUTES)
    }

    override fun loadNfcTags(): Completable {
        return syncDao.getRoutes()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { route ->
                taskDao.getExecutorIdByRouteId(route.id)
                    .flatMapCompletable { executorId ->
                        if (preferencesRepository.executorId == executorId) {
                            val filter = NfcFilter.getFilterByRouteId(route.id)
                            loadAcronEntities(NFC_ROUTE, NfcRouteColumns.getColumns(), filter)
                        } else Completable.complete()
                    }

            }.catchAcronException(NFC_ROUTE)
    }

    override fun loadCheckLists(): Completable {
        return syncDao.getRoutes()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { route ->
                taskDao.getExecutorIdByRouteId(route.id)
                    .flatMapCompletable { executorId ->
                        if (preferencesRepository.executorId == executorId) {
                            val filter = CheckListFilter.getFilterByRouteId(route.id)
                            loadAcronEntities(CHECK_LISTS, CheckListColumns.getColumns(), filter)
                        } else Completable.complete()
                    }
            }.catchAcronException(CHECK_LISTS)
    }

    override fun loadAnswers(): Completable {
        return syncDao.getCheckLists(DatabaseConstants.TYPE_ANSWER_TEMPLATE)
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable {
                val filter = AnswerFilter.getFilterByCheckListId(it.id)
                loadAcronEntities(ANSWER, AnswerColumns.getColumns(), filter)
            }.catchAcronException(ANSWER)
    }

    private fun handleApi(
        functionName: String,
        requestBody: GettingDataRequest
    ): Single<SelectResponse> {
        return if (functionName == USER_GROUPS) {
            userApi.getUserGroups(requestBody)
        } else functionsApi.getData(requestBody)
    }

    @Suppress("UNCHECKED_CAST")
    private fun handleSaving(functionName: String, entityList: List<AcronEntity>): Completable {
        return when (functionName) {
            DIRECTORIES -> syncDao.saveDirectories(entityList as List<Directory>)
            NOTIFICATIONS -> syncDao.saveNotifications(entityList as List<Notification>)
            TASKS -> syncDao.saveTasks(entityList as List<Task>)
            EQUIPMENT_CLASS -> syncDao.saveEquipmentClasses(entityList as List<EquipmentClass>)
            EQUIPMENT -> syncDao.saveEquipmentList(entityList as List<Equipment>)
            NFC_EQUIPMENT -> syncDao.saveNfcEquipmentList(entityList as List<NfcEquipment>)
            NFC_ROUTE -> syncDao.saveNfcRoutes(entityList as List<NfcRoute>)
            ROUTES -> syncDao.saveRoutes(entityList as List<Route>)
            CHECK_LISTS -> syncDao.saveCheckLists(entityList as List<CheckList>)
            EXECUTORS -> syncDao.saveExecutors(entityList as List<Executor>)
            DEFECT_CAUSES -> syncDao.saveDefectCauses(entityList as List<DefectCause>)
            DEFECTS -> syncDao.saveDefects(entityList as List<Defect>)
            USER_GROUPS -> syncDao.saveGroups(entityList as List<UserGroup>)
            DEFECT_RELATIONS -> Completable.concatArray(
                syncDao.deleteDefectRelations(),
                syncDao.saveDefectRelations(entityList as List<DefectRelation>)
            )

            ANSWER -> syncDao.saveAnswers(entityList as List<Answer>)
            DEFECT_LOG -> syncDao.saveDefectsLogs(entityList as List<DefectLog>)
            else -> throw IllegalArgumentException("Unknown function name: $functionName")
        }
    }

    override fun uploadLocalDefects(): Completable {
        return Single.zip(
            localDefectDao.getLocalDefectsInTasksForUploading(),
            localDefectDao.getLocalDefectsInEquipmentsForUploading(),
            BiFunction<List<LocalDefect>, List<LocalDefect>, List<LocalDefect>>
            { defectsFromCompletedTasks, defectsFromEquipments ->
                val allDefectsForUploading = mutableListOf<LocalDefect>()
                allDefectsForUploading.addAll(defectsFromCompletedTasks)
                allDefectsForUploading.addAll(defectsFromEquipments)
                allDefectsForUploading.toList()
            }).toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { localDefect ->
                val criticality = if (localDefect.criticality == Constants.CRITICALITY_NO) {
                    Constants.ACRON_EMPTY
                } else localDefect.criticality.toString()

                val defectLogValue = DefectLogValue(
                    localDefect.equipmentId.toAcronId(), localDefect.dateCreation,
                    localDefect.defectNameId.toAcronId(), localDefect.defectCauseId.toAcronId(),
                    criticality, localDefect.checkListId.toAcronId(),
                    localDefect.comment
                )

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    DEFECT_LOG, defectLogValue
                )

                functionsApi.insertDefect(requestBody)
                    .flatMapCompletable { response ->
                        updateMediaFilesIds(localDefect, response.defectLogId)
                    }.andThen(localDefectDao.deleteLocalDefectById(localDefect.id))
            }.catchAcronException(DEFECT_LOG)
    }

    //Update defectLogId of each MediaFile of this defect with defectLogId from the response
    private fun updateMediaFilesIds(localDefect: LocalDefect, newDefectId: Int): Completable {
        return getMediaFilesForUpdating(localDefect)
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { mediaFile ->
                mediaFileDao.updateMediaFileDefectLogId(mediaFile.id, newDefectId)
            }
    }

    private fun getMediaFilesForUpdating(localDefect: LocalDefect): Single<List<MediaFile>> {
        return if (localDefect.checkListId == DEFAULT_INVALID_ID) {
            //This defect was created from equipment
            mediaFileDao.getDatabaseMediaFilesByDefectLogId(localDefect.id)
        } else {
            //This defect was created from check list
            mediaFileDao.getDatabaseMediaFilesByCheckListId(localDefect.checkListId)
        }
    }

    override fun uploadTaskStatuses(): Completable {
        return taskDao.getTasksByExecutorId(
            arrayListOf(
                DatabaseConstants.TASK_STATUS_NEW,
                DatabaseConstants.TASK_STATUS_IN_PROGRESS
            ), preferencesRepository.executorId
        )
            .firstOrError()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { task ->
                val taskValue = TaskStatusValue(
                    task.id.toString(), task.status.toString(),
                    task.startDateActual, task.notificationDate
                )

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    TASKS, taskValue
                )

                functionsApi.updateData(requestBody)
            }.catchAcronException(TASKS)
    }

    override fun uploadTasks(): Completable {
        return taskDao.getTasksByStatusCodes(
            arrayListOf(
                DatabaseConstants.TASK_STATUS_COMPLETED
            )
        ).firstOrError()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { task ->
                val taskValue = TaskValue(
                    task.id.toString(), task.status.toString(), task.startDateActual,
                    task.endDateActual, task.unansweredCheckLists.toString(),
                    task.comment, task.notificationDate, task.defectsCount.toString(),
                    task.attachmentsCount.toString(),
                    task.deviceIdOnStart, task.deviceIdOnFinish
                )

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    TASKS, taskValue
                )

                DataLogStorage.setUploadTasks(taskValue)

                mediaFileDao.getMediaFilesByTaskToDelete(task.id)
                    .flatMapIterable { it }
                    .flatMapCompletable { displayMediaFile ->
                        Completable.fromAction {
                            val file = File(displayMediaFile.filePath)
                            if (file.exists()) {
                                file.delete()
                            }
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

                Completable.concatArray(
                    functionsApi.updateData(requestBody),
                    mediaFileDao.deleteMediaFilesByTaskId(task.id),
                    notificationDao.deleteNotification(task.id),
                    checkListDao.deleteCheckListByTaskId(task.id),
                    routeDao.deleteRouteByTaskId(task.id),
                    taskDao.deleteTaskById(task.id)
                )
            }.catchAcronException(TASKS)
    }


    override fun uploadRoutes(): Completable {
        return routeDao.getRoutesForUploading(DatabaseConstants.TASK_STATUS_COMPLETED)
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { route ->
                val statusCode = when {
                    route.endDateActual.isNotEmpty() -> DatabaseConstants.TASK_STATUS_COMPLETED
                    route.startDateActual.isNotEmpty() -> DatabaseConstants.TASK_STATUS_IN_PROGRESS
                    else -> DatabaseConstants.TASK_STATUS_NEW
                }

                val routeValue = RouteValue(
                    route.id.toString(), route.number.toString(),
                    route.answeredQuestions.toString(), route.answeredNfcMarks.toString(),
                    statusCode.toString(), route.startDateActual, route.endDateActual
                )

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    ROUTES, routeValue
                )

                DataLogStorage.setUploadRoutes(routeValue)

                functionsApi.updateData(requestBody)
            }.catchAcronException(ROUTES)
    }

    override fun uploadCheckLists(): Completable {
        return checkListDao.getCheckListsForUploading()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { checkList ->
                val isDefect = if (checkList.isDefect) {
                    Constants.ACRON_TRUE
                } else Constants.ACRON_FALSE

                val attachmentsCount = if (checkList.isDefect) {
                    checkList.attachmentsCount.toString()
                } else ""

                val checkListValue = CheckListValue(
                    checkList.checkListId.toString(), isDefect,
                    attachmentsCount, checkList.comment, checkList.answerDate
                )

                if (checkList.typeAnswer == DatabaseConstants.TYPE_ANSWER_VALUE) {
                    checkListValue.answerNumber = if (checkList.answer.isEmpty()) {
                        ((checkList.minValue + checkList.maxValue) / 2).toInt().toString()
                    } else checkList.answer.deleteSpaces()
                } else checkListValue.answer = checkList.answer

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    CHECK_LISTS, checkListValue
                )

                DataLogStorage.setUploadCheckListValue(checkListValue)

                functionsApi.updateData(requestBody)
            }.catchAcronException(CHECK_LISTS)
    }

    override fun uploadRoutesNfcTags(): Completable {
        return nfcRouteDao.getNfcRouteTagsForUploading()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { nfcRoute ->
                val nfcRouteValue = NfcRouteValue(
                    nfcRoute.id.toString(),
                    nfcRoute.routeId.toString(), nfcRoute.time
                )

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    NFC_ROUTE, nfcRouteValue
                )

                DataLogStorage.setUploadRoutesNfcTags(nfcRouteValue)

                functionsApi.updateData(requestBody)
            }.catchAcronException(NFC_ROUTE)
    }

    override fun uploadEquipmentsNfcTags(): Completable {
        return nfcEquipmentDao.getNfcEquipmentTagsForUploading()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { nfcEquipment ->
                val nfcEquipmentValue = NfcEquipmentValue(
                    nfcEquipment.equipmentId.toString(),
                    nfcEquipment.name, nfcEquipment.code
                )

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    NFC_EQUIPMENT, nfcEquipmentValue
                )

                Completable.concatArray(
                    functionsApi.insertData(requestBody),
                    nfcEquipmentDao.deleteNfcEquipmentTag(nfcEquipment)
                )
            }.catchAcronException(NFC_EQUIPMENT)
    }

    override fun uploadAttachments(): Completable {
        return Single.zip(
            mediaFileDao.getMediaFilesInTasksForUploading(),
            mediaFileDao.getMediaFilesInDefectsForUploading(),
            BiFunction<List<MediaFileWithHash>, List<MediaFileWithHash>, List<MediaFileWithHash>>
            { mediaFilesInCompletedTasks, mediaFilesInDefects ->
                val allMediaFiles = mutableListOf<MediaFileWithHash>()
                allMediaFiles.addAll(mediaFilesInCompletedTasks)
                allMediaFiles.addAll(mediaFilesInDefects)
                allMediaFiles.toList()
            })
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { mediaFile ->
                //Get binary from file path
                val requestBody = getRequestBodyFromFilePath(mediaFile.filePath)

                //Upload attachment to storage for getting file hash
                uploadAttachmentToStorage(sessionId, functionId, requestBody)
                    .flatMapCompletable { hash ->
                        mediaFile.hash = hash

                        //Upload media file to server
                        uploadAttachmentToServer(mediaFile)
                    }
            }.catchAcronException(ATTACHMENTS)
    }

    override fun uploadDataLog(): Completable {
        if (DataLogStorage.file.isEmpty()) {
            return Completable.error(IllegalArgumentException("No files to upload"))
        }
        val completables = DataLogStorage.file.map { file ->
            if (file.exists()) {
                if (file.isDirectory) {
                    Completable.complete()
                } else {
                    val requestFile =
                        file.readBytes().toRequestBody("text/plain".toMediaTypeOrNull())
                    uploadAttachmentToStorageLog(sessionId, functionId, requestFile)
                        .flatMapCompletable {
                            Completable.complete()
                        }
                }
            } else {
                Completable.error(IllegalArgumentException("File does not exist: ${file.absolutePath}"))
            }
        }
        return Completable.merge(completables)
            .doOnComplete {
                DataLogStorage.deleteFiles()
            }
    }

    private fun getRequestBodyFromFilePath(filePath: String): RequestBody {
        val bytes = getByteArrayFromFilePath(filePath)
        val mediaType = "application/octet-stream".toMediaTypeOrNull()

        return bytes.toRequestBody(mediaType)
    }

    private fun getByteArrayFromFilePath(filePath: String): ByteArray {
        val fileInputStream = FileInputStream(File(filePath))

        return try {
            val maxBufferSize = NetworkConstants.FILE_MAX_SIZE
            val bytesAvailable = fileInputStream.available()
            val bufferSize = min(maxBufferSize, bytesAvailable)
            val buffer = ByteArray(bufferSize)
            fileInputStream.read(buffer, 0, bufferSize)

            buffer
        } catch (exception: Exception) {
            throw exception
        } finally {
            fileInputStream.close()
        }
    }

    private fun uploadAttachmentToStorage(
        sessionId: String, functionId: Int,
        requestBody: RequestBody
    ): Single<String> {
        return functionsApi.uploadAttachment(
            sessionId = sessionId,
            functionId = functionId,
            storageId = NetworkConstants.STORAGE_ID,
            requestBody = requestBody
        )
    }

    private fun uploadAttachmentToStorageLog(
        sessionId: String, functionId: Int,
        requestBody: RequestBody
    ): Single<String> {
        return functionsApi.uploadAttachment(
            sessionId = sessionId,
            functionId = functionId,
            storageId = NetworkConstants.LOGS_STORAGE_ID,
            requestBody = requestBody
        )
    }

    private fun uploadAttachmentToServer(mediaFile: MediaFileWithHash): Completable {
        val attachmentType = when (mediaFile.mediaType) {
            Constants.MEDIA_TYPE_PHOTO -> NetworkConstants.DOC_TYPE_PHOTO
            Constants.MEDIA_TYPE_VIDEO -> NetworkConstants.DOC_TYPE_VIDEO
            Constants.MEDIA_TYPE_AUDIO -> NetworkConstants.DOC_TYPE_AUDIO
            else -> throw IllegalArgumentException("Unknown media type: ${mediaFile.mediaType}")
        }

        val attachmentValue = AttachmentValue(
            mediaFile.checkListId.toAcronId(),
            mediaFile.defectLogId.toAcronId(), attachmentType,
            mediaFile.filePath.filePathToFileName(), mediaFile.hash
        )

        val requestBody = ModifyingDataRequest(
            sessionId, functionId,
            ATTACHMENTS, attachmentValue
        )

        return Completable.concatArray(
            functionsApi.insertData(requestBody),
            mediaFileDao.mediaFileUploaded(mediaFile.id)
        )
    }

    override fun changeTaskExecutors(): Completable {
        return taskDao.getTasksForChangeExecutors(DatabaseConstants.TASK_STATUS_NEW)
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable {
                val taskExecutorValue = TaskExecutorValue(
                    it.id.toString(),
                    it.executorId.toString()
                )

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    TASKS, taskExecutorValue
                )

                functionsApi.changeExecutorId(requestBody)
            }.catchAcronException(EXECUTORS)
    }

    override fun changeRouteOrderNumbers(): Completable {
        return routeDao.getRoutesForUploading(DatabaseConstants.TASK_STATUS_NEW)
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable {
                val routeOrderNumberValue = RouteOrderNumberValue(
                    it.id.toString(),
                    it.number.toString()
                )

                val requestBody = ModifyingDataRequest(
                    sessionId, functionId,
                    ROUTES, routeOrderNumberValue
                )

                functionsApi.changeOrderNumber(requestBody)
            }.catchAcronException(ROUTES)
    }
}