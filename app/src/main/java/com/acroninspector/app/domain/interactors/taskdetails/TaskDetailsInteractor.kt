package com.acroninspector.app.domain.interactors.taskdetails

import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface TaskDetailsInteractor {

    fun getRoutesByTaskId(taskId: Int): Flowable<List<DisplayRoute>>

    fun getTaskById(id: Int): Flowable<DisplayTask>

    fun startRoute(taskId: Int, status: Int, actualStartDate: String): Completable

    fun finishRoute(taskId: Int, status: Int, actualEndDate: String): Completable

    fun getUnansweredLists(routes: List<DisplayRoute>): Single<String>

    fun getUnansweredNfcMarks(routes: List<DisplayRoute>): Single<String>

    fun getExecutorId(): Int

    fun hasNoDefectsForEquipment(
        routeId: Int,
    ): Completable
}