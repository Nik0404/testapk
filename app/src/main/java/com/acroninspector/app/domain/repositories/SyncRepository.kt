package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.remote.request.other.Filter
import io.reactivex.Completable

interface SyncRepository {

    fun setApiIds(functionId: Int, sessionId: String)

    fun loadAcronEntities(
            functionName: String,
            columns: List<String> = listOf(),
            filter: List<Filter> = listOf()
    ): Completable

    fun loadTasks(functionId: Int): Completable

    fun loadRoutes(): Completable

    fun loadNfcTags(): Completable

    fun loadCheckLists(): Completable

    fun loadAnswers(): Completable

    fun uploadLocalDefects(): Completable

    fun uploadTaskStatuses(): Completable

    fun uploadTasks(): Completable

    fun uploadRoutes(): Completable

    fun uploadCheckLists(): Completable

    fun uploadEquipmentsNfcTags(): Completable

    fun uploadRoutesNfcTags(): Completable

    fun uploadAttachments(): Completable

    fun changeRouteOrderNumbers(): Completable

    fun changeTaskExecutors(): Completable

    fun uploadDataLog(): Completable
}