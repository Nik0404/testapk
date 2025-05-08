package com.acroninspector.app.domain.repositories

interface PreferencesRepository {

    var login: String
    var password: String

    var cardid: String
    var pin: String

    val userId: Int
    var executorId: Int
    var executorGroupIds: List<Int>
    var supervisedDivisionId: Int

    var functionId: Int
    var authSessionId: String
    var functionSessionId: String

    var versionName: String

    var isDataLoadedFromServer: Boolean
    var isDataUploadedToServer: Boolean

    var deviceId: String

    fun clear()
}
