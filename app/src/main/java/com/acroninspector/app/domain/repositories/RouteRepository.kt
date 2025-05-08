package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.Route
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface RouteRepository {

    fun getRouteById(routeId: Int): Flowable<DisplayRoute>

    fun getRouteNameByNfcCode(nfcCode: String): Single<String>

    fun getRoutesByTaskIdFromDatabase(taskId: Int): Flowable<List<DisplayRoute>>

    fun getRoutesByTaskIdFromNetwork(sessionId: String, functionId: Int, taskId: Int): Flowable<List<Route>>

    fun getControlProceduresByTaskId(taskId: Int): Single<List<DisplayControlProcedure>>

    fun updateStartDateActual(routeId: Int, startDate: String): Completable

    fun updateEndDateActual(routeId: Int, endDate: String): Completable

    fun getUnansweredLists(routes: List<DisplayRoute>): Single<String>

    fun increaseUnansweredLists(routeId: Int): Completable

    fun decreaseUnansweredLists(routeId: Int): Completable

    fun updateRoute(number: Int, routeId: Int): Completable

    fun checkIfRouteExists(routeId: Int): Maybe<Int>

    fun setListAnsweredCount(routeId: Int, answersCount: Int): Completable

}
