package com.acroninspector.app.domain.entity.remote.request.values

import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

object DataLogStorage {

    val uploadCheckListValue = CopyOnWriteArrayList<CheckListValue>()

    val uploadRoutes = CopyOnWriteArrayList<RouteValue>()

    val uploadRoutesNfcTags = CopyOnWriteArrayList<NfcRouteValue>()

    val uploadTasks = CopyOnWriteArrayList<TaskValue>()

    var file = CopyOnWriteArrayList<File>()

    var login = ""

    var deviceId = ""

    var error = ""

    var date = ""

    fun setUploadCheckListValue(_uploadCheckListValue: CheckListValue) {
        uploadCheckListValue.add(_uploadCheckListValue)
    }

    fun setUploadRoutes(_uploadRoutes: RouteValue) {
        uploadRoutes.add(_uploadRoutes)
    }

    fun setUploadRoutesNfcTags(_uploadRoutesNfcTags: NfcRouteValue) {
        uploadRoutesNfcTags.add(_uploadRoutesNfcTags)
    }

    fun setUploadTasks(_uploadTasks: TaskValue) {
        uploadTasks.add(_uploadTasks)
    }

    fun getDataLog(): DataLog {
        return DataLog(
            date,
            login,
            deviceId,
            uploadCheckListValue,
            uploadRoutes,
            uploadRoutesNfcTags,
            uploadTasks,
            error
        )
    }

    fun deleteFiles() {
        val filesToDelete = file.toList()

        for (f in filesToDelete) {
            if (f.exists()) {
                f.delete()
            }
        }
        clearAllData()
    }

    fun clearAllData() {
        uploadCheckListValue.clear()
        uploadRoutes.clear()
        uploadRoutesNfcTags.clear()
        uploadTasks.clear()
        file.clear()

        login = ""
        deviceId = ""
        error = ""
        date = ""
    }
}
