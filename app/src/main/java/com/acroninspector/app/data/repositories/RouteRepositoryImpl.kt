package com.acroninspector.app.data.repositories

import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.datasource.database.dao.RouteDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.util.constants.RouteColumns
import com.acroninspector.app.data.util.filters.RouteFilter
import com.acroninspector.app.data.util.mappers.RouteMapper
import com.acroninspector.app.domain.entity.local.database.Route
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.entity.remote.request.GettingDataRequest
import com.acroninspector.app.domain.entity.remote.request.other.Order
import com.acroninspector.app.domain.repositories.RouteRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class RouteRepositoryImpl(
    private val functionsApi: FunctionsApi,
    private val routeDao: RouteDao,
    private val routeMapper: RouteMapper
) : RouteRepository {

    override fun getRouteById(routeId: Int): Flowable<DisplayRoute> {
        return routeDao.getRouteById(routeId)
    }

    override fun getRouteNameByNfcCode(nfcCode: String): Single<String> {
        return routeDao.getRouteNameByNfcCode(nfcCode)
    }

    override fun getRoutesByTaskIdFromDatabase(taskId: Int): Flowable<List<DisplayRoute>> {
        return routeDao.getAllRoutes(taskId)
    }

    override fun getControlProceduresByTaskId(taskId: Int): Single<List<DisplayControlProcedure>> {
        return routeDao.getControlProceduresByTaskId(taskId)
    }

    override fun getRoutesByTaskIdFromNetwork(
        sessionId: String,
        functionId: Int,
        taskId: Int
    ): Flowable<List<Route>> {
        val order = listOf(
            Order(
                RouteColumns.ORDER_NUMBER_COLUMN_NAME,
                NetworkConstants.ORDER_ASC
            )
        )
        val requestBody = GettingDataRequest(
            sessionId = sessionId,
            functionId = functionId,
            functionName = FunctionNames.ROUTES,
            columns = RouteColumns.getColumns(),
            filter = RouteFilter.getFilterByTaskId(taskId),
            order = order
        )

        return functionsApi.getData(requestBody)
            .map { response ->
                val routes = mutableListOf<Route>()

                response.values.forEach {
                    val routeItem = routeMapper.fromSchemaToEntity(it)
                    routes.add(routeItem as Route)
                }

                routes.toList()
            }.toFlowable()
    }

    override fun updateStartDateActual(routeId: Int, startDate: String): Completable {
        return routeDao.updateStartDateActual(routeId, startDate)
    }

    override fun updateEndDateActual(routeId: Int, endDate: String): Completable {
        return routeDao.updateEndDateActual(routeId, endDate)
    }

    override fun getUnansweredLists(routes: List<DisplayRoute>): Single<String> {
        return Single.create<String> { emitter ->
            val unansweredRoutesNumbers = StringBuilder()

            for (route in routes) {
                if (route.answeredQuestions != route.questions) {
                    unansweredRoutesNumbers.append(route.number).append(", ")
                }
            }

            val result = try {
                unansweredRoutesNumbers.substring(0, unansweredRoutesNumbers.length - 2)
            } catch (e: Exception) {
                ""
            }

            emitter.onSuccess(result.toString())
        }
    }

    override fun increaseUnansweredLists(routeId: Int): Completable {
        return routeDao.increaseUnansweredLists(routeId)
    }

    override fun decreaseUnansweredLists(routeId: Int): Completable {
        return routeDao.decreaseUnansweredLists(routeId)
    }

    override fun updateRoute(number: Int, routeId: Int): Completable {
        return routeDao.updateRoute(number, routeId)
    }

    override fun checkIfRouteExists(routeId: Int): Maybe<Int> {
        return routeDao.checkIfRouteExists(routeId)
    }

    override fun setListAnsweredCount(routeId: Int, answersCount: Int): Completable {
        return routeDao.setListAnswered(routeId, answersCount)
    }
}
