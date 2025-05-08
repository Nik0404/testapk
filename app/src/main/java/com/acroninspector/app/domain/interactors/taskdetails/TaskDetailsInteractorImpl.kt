package com.acroninspector.app.domain.interactors.taskdetails

import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.Route
import com.acroninspector.app.domain.entity.local.display.DisplayEquipmentItem
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.repositories.CheckListRepository
import com.acroninspector.app.domain.repositories.EquipmentRepository
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import com.acroninspector.app.domain.repositories.NfcRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.RouteRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.Calendar

class TaskDetailsInteractorImpl(
    private val routeRepository: RouteRepository,
    private val taskRepository: TaskRepository,
    private val equipmentRepository: EquipmentRepository,
    private val nfcRepository: NfcRepository,
    private val preferencesRepository: PreferencesRepository,
    private val localDefectRepository: LocalDefectRepository,
    private val schedulersProvider: SchedulersProvider,
    private val checkListRepository: CheckListRepository,
) : TaskDetailsInteractor {

    override fun getRoutesByTaskId(taskId: Int): Flowable<List<DisplayRoute>> {
        return getRoutesByTaskIdFromDatabase(taskId)
            .subscribeOn(schedulersProvider.io())
            .flatMap {
                if (it.isEmpty()) {
                    getRoutesByTaskIdFromNetwork(taskId)
                } else Flowable.just(it)
            }.observeOn(schedulersProvider.ui())
    }

    private fun getRoutesByTaskIdFromNetwork(taskId: Int): Flowable<List<DisplayRoute>> {
        val sessionId = preferencesRepository.functionSessionId
        val functionId = preferencesRepository.functionId

        return Flowable.zip(
            routeRepository.getRoutesByTaskIdFromNetwork(sessionId, functionId, taskId),
            equipmentRepository.getAllEquipments(),
            BiFunction<List<Route>, List<DisplayEquipmentItem>, List<DisplayRoute>>
            { routes, equipments ->
                val displayRoutes: MutableList<DisplayRoute> = mutableListOf()

                routes.forEach { route ->
                    val equipment = getEquipmentById(equipments, route.equipmentId)
                    val displayRoute = DisplayRoute(
                        route.id, route.taskId, route.equipmentId, route.orderNumber,
                        equipment?.name!!, equipment.code, equipment.className,
                        route.countQuestion, route.countAnswer, route.countNfc,
                        route.countNfcAnswered, 0, 0
                    )
                    displayRoutes.add(displayRoute)
                }

                displayRoutes.toList()
            })
            .subscribeOn(schedulersProvider.io())
            .onErrorResumeNext(Flowable.just(arrayListOf()))
            .observeOn(schedulersProvider.ui())
    }

    private fun getRoutesByTaskIdFromDatabase(taskId: Int): Flowable<List<DisplayRoute>> {
        return routeRepository.getRoutesByTaskIdFromDatabase(taskId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    private fun getEquipmentById(
        equipments: List<DisplayEquipmentItem>,
        equipmentId: Int
    ): DisplayEquipmentItem? {
        equipments.forEach { equipment ->
            if (equipment.id == equipmentId) {
                return equipment
            }
        }
        return null
    }

    override fun getUnansweredLists(routes: List<DisplayRoute>): Single<String> {
        return routeRepository.getUnansweredLists(routes)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    override fun getUnansweredNfcMarks(routes: List<DisplayRoute>): Single<String> {
        return nfcRepository.getUnansweredNfcMarks(routes)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    override fun getTaskById(id: Int): Flowable<DisplayTask> {
        return taskRepository.getTaskById(id)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .flatMap { task ->
                taskRepository.checkIfHasStarted(task.executorId, task.id)
                    .subscribeOn(schedulersProvider.io())
                    .observeOn(schedulersProvider.ui())
                    .map { startedTasksAmount ->
                        startedTasksAmount > 0
                        if (startedTasksAmount > 0) {
                            return@map task.copy(status = 40)
                        }
                        task
                    }
            }


    }

    override fun startRoute(taskId: Int, status: Int, actualStartDate: String): Completable {
        return Completable.concatArray(
            taskRepository.updateTaskStatus(taskId, status),
            taskRepository.updateTaskActualStartDate(taskId, actualStartDate),
            taskRepository.updateTaskStartDevice(taskId, preferencesRepository.deviceId),
            fillRouteStartDates(taskId, actualStartDate)
        ).doOnComplete { preferencesRepository.isDataUploadedToServer = false }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    private fun fillRouteStartDates(taskId: Int, startDate: String): Completable {
        return routeRepository.getRoutesByTaskIdFromDatabase(taskId).firstOrError()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { routeRepository.updateStartDateActual(it.id, startDate) }
    }

    override fun finishRoute(taskId: Int, status: Int, actualEndDate: String): Completable {
        return Completable.concatArray(
            taskRepository.updateTaskStatus(taskId, status),
            taskRepository.updateTaskActualEndDate(taskId, actualEndDate),
            taskRepository.updateTaskFinishDevice(taskId, preferencesRepository.deviceId),
            completeTaskDates(taskId),
            completeRouteDates(taskId),
            deleteExtraLocalDefectsByTaskId(taskId)
        ).doOnComplete { preferencesRepository.isDataUploadedToServer = false }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    private fun completeTaskDates(taskId: Int): Completable {
        return taskRepository.getTaskById(taskId).firstOrError()
            .flatMapCompletable { task ->
                if (task.notificationDate.isEmpty()) {
                    taskRepository.updateNotificationDate(taskId, task.startDateActual)
                } else Completable.complete()
            }
    }

    private fun completeRouteDates(taskId: Int): Completable {
        return routeRepository.getRoutesByTaskIdFromDatabase(taskId).firstOrError()
            .toObservable()
            .flatMapIterable { it }
            .flatMapCompletable { route ->
                if (route.endDateActual.isEmpty()) {
                    routeRepository.updateEndDateActual(route.id, route.startDateActual)
                } else Completable.complete()
            }
    }

    private fun deleteExtraLocalDefectsByTaskId(taskId: Int): Completable {
        return localDefectRepository.getExtraLocalDefectsByCompletedTaskId(taskId)
            .flatMapCompletable { localDefectRepository.deleteLocalDefects(it) }
    }

    override fun getExecutorId(): Int = preferencesRepository.executorId

    override fun hasNoDefectsForEquipment(
        routeId: Int,
    ): Completable {
        return checkListRepository.getQuestionIds(routeId)
            .flatMapCompletable { questionIds ->
                Flowable.fromIterable(questionIds)
                    .filter { it.routeId == routeId }
                    .flatMapCompletable { questionId ->
                        updateAnswer(
                            routeId = routeId,
                            questionId = questionId.id
                        )
                    }
                    .andThen(updateUnansweredLists(routeId, questionIds.size))
            }
            .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    private fun updateAnswer(
        routeId: Int,
        questionId: Int,
    ): Completable {
        val answerDate = DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)
        val answer = "1"
        return checkListRepository.getQuestionAnswer(questionId)
            .flatMapCompletable { currentAnswer ->
                val updateAnswerCompletable =
                    checkListRepository.updateAnswer(questionId, answer, answerDate, false)
                val updateEndDateCompletable =
                    routeRepository.updateEndDateActual(routeId, answerDate)

                Completable.concatArray(
                    updateAnswerCompletable,
                    updateEndDateCompletable,
                )
            }
            .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    private fun updateUnansweredLists(routeId: Int, questionCount: Int): Completable {
        return routeRepository.setListAnsweredCount(routeId, questionCount)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }
}