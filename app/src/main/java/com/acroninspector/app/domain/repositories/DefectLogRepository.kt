package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.DefectLog
import com.acroninspector.app.domain.entity.remote.request.other.Filter
import io.reactivex.Single

interface DefectLogRepository {

    fun getDefectLogsFromNetwork(
        sessionId: String,
        functionId: Int,
        filter: List<Filter>
    ): Single<List<DefectLog>>
}
