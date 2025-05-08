package com.acroninspector.app.domain.interactors.sync

import io.reactivex.Flowable

interface SyncInteractor {

    fun registerFunctionAndLoadDataFromServer(): Flowable<Int>

    fun loadDataFromServer(): Flowable<Int>

    fun uploadDataToServer(): Flowable<Int>

    fun dataLoadedFromSeverSuccessfully()

    fun dataUploadedToSeverSuccessfully()

    fun isDataLoadedFromServer(): Boolean

    fun isDataUploadedToServer(): Boolean

    fun getLoadingEntityCountByFunction(): Int

    fun uploadDataLogToServer()
}
