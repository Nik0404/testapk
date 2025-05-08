package com.acroninspector.app.domain.entity.remote.request.values

import java.util.concurrent.CopyOnWriteArrayList

data class DataLog(
    val date: String,
    val login: String,
    val deviceId: String,
    val uploadCheckListValue: CopyOnWriteArrayList<CheckListValue>,
    val uploadRoutes: CopyOnWriteArrayList<RouteValue>,
    val uploadRoutesNfcTags: CopyOnWriteArrayList<NfcRouteValue>,
    val uploadTasks: CopyOnWriteArrayList<TaskValue>,
    val error: String
)
