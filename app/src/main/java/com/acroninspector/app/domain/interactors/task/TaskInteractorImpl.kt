package com.acroninspector.app.domain.interactors.task

import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.Task
import com.acroninspector.app.domain.entity.local.display.DisplayExecutor
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.repositories.ExecutorRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class TaskInteractorImpl(
        private val taskRepository: TaskRepository,
        private val executorRepository: ExecutorRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : TaskInteractor {

    override fun getCompletedTasks(): Flowable<List<DisplayTask>> {
        return Flowable.combineLatest(
                getCompletedTasksFromNetwork(),
                getCompletedTasksFromDatabase(),
                BiFunction { listFromNetwork, listFromDatabase ->
                    val displayTasks: MutableList<DisplayTask> = ArrayList()

                    displayTasks.addAll(listFromDatabase)
                    displayTasks.addAll(listFromNetwork)

                    return@BiFunction displayTasks
                }
        )
    }

    private fun getCompletedTasksFromDatabase(): Flowable<List<DisplayTask>> {
        val statusCode = arrayListOf(DatabaseConstants.TASK_STATUS_COMPLETED)
        return taskRepository.getTasksByStatusCodes(statusCode)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    private fun getCompletedTasksFromNetwork(): Flowable<List<DisplayTask>> {
        return taskRepository.getCompletedTasksFromNetwork(
                preferencesRepository.functionSessionId,
                preferencesRepository.functionId,
                preferencesRepository.executorGroupIds,
                preferencesRepository.supervisedDivisionId
        ).zipWith(executorRepository.getAllExecutors().toFlowable(),
                BiFunction<List<Task>, List<DisplayExecutor>, List<DisplayTask>> { tasks, executors ->
                    val displayTasks: MutableList<DisplayTask> = mutableListOf()

                    tasks.forEach { task ->
                        val displayTask = DisplayTask(task.id, task.executorGroupId,
                                task.taskNumber, task.taskName, task.executorId,
                                findExecutorById(executors, task.executorId), task.startDatePlan,
                                task.endDatePlan, task.countChecklist, 0,
                                task.attachmentsCount, task.taskStatusCode, task.notificationDate, task.deviceIdOnStart, task.deviceIdOnFinish)
                        displayTask.startDateActual = task.startDateFact
                        displayTask.endDateActual = task.endDateFact
                        displayTask.comment = task.userComment
                        displayTask.unansweredCheckLists = task.countChecklist - task.unansweredChecklist

                        displayTasks.add(displayTask)
                    }

                    return@BiFunction displayTasks.toList()
                }).subscribeOn(schedulersProvider.io())
                .onErrorResumeNext(Flowable.just(arrayListOf()))
                .observeOn(schedulersProvider.ui())
    }

    private fun findExecutorById(executors: List<DisplayExecutor>, executorId: Int): String {
        executors.forEach { executor ->
            if (executor.id == executorId) {
                return executor.shortName
            }
        }
        return ""
    }

    override fun getTasksByStatusCodes(statusCode: ArrayList<Int>): Flowable<List<DisplayTask>> {
        return taskRepository.getTasksByStatusCodes(statusCode)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}
