package com.acroninspector.app.data.repositories

import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.datasource.database.dao.DefectLogDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.util.constants.DefectLogColumns
import com.acroninspector.app.data.util.mappers.DefectLogMapper
import com.acroninspector.app.domain.entity.local.database.DefectLog
import com.acroninspector.app.domain.entity.remote.request.GettingDataRequest
import com.acroninspector.app.domain.entity.remote.request.other.Filter
import com.acroninspector.app.domain.entity.remote.request.other.Order
import com.acroninspector.app.domain.repositories.DefectLogRepository
import io.reactivex.Single

class DefectLogRepositoryImpl(
        private val functionsApi: FunctionsApi,
        private val defectLogDao: DefectLogDao,
        private val defectLogMapper: DefectLogMapper
) : DefectLogRepository {

    override fun getDefectLogsFromNetwork(
        sessionId: String,
        functionId: Int,
        filter: List<Filter>
    ): Single<List<DefectLog>> {
        val order = listOf(
            Order(
                DefectLogColumns.DEFECT_DT_COLUMN_NAME,
                NetworkConstants.ORDER_DESC
            )
        )
        val requestBody = GettingDataRequest(
            sessionId = sessionId,
            functionId = functionId,
            functionName = FunctionNames.DEFECT_LOG,
            columns = DefectLogColumns.getColumns(),
            filter = filter,// DefectLogFilter.getFilterByDivision(divisionId),
            order = order
        )

        return functionsApi.getData(requestBody)
                .map { response ->
                    val defectLogs = mutableListOf<DefectLog>()

                    response.values.forEach {
                        val defectLogItem = defectLogMapper.fromSchemaToEntity(it)
                        defectLogs.add(defectLogItem as DefectLog)
                    }

                    defectLogs.toList()
                }
    }
}
