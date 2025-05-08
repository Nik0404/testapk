package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.Task
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface TaskRepository {

    fun getCompletedTasksFromNetwork(
            sessionId: String,
            functionId: Int,
            executorGroupIds: List<Int>,
            divisionId: Int
    ): Flowable<List<Task>>

    fun getTasksByStatusCodes(statusCode: ArrayList<Int>): Flowable<List<DisplayTask>>

    fun getTaskById(id: Int): Flowable<DisplayTask>

    fun getExecutorIdByTaskId(id: Int): Flowable<Int>

    fun updateTaskComment(taskId: Int, comment: String): Completable

    fun updateTaskStatus(taskId: Int, status: Int): Completable

    fun updateTaskActualStartDate(taskId: Int, actualStartDate: String): Completable

    fun updateTaskActualEndDate(taskId: Int, actualEndDate: String): Completable

    fun updateTaskExecutor(taskId: Int, executorId: Int): Completable

    fun updateNotificationDate(taskId: Int, notificationDate: String): Completable

    fun checkIfTaskExists(taskId: Int): Maybe<Int>

    fun updateTaskStartDevice(taskId: Int, deviceId: String): Completable

    fun updateTaskFinishDevice(taskId: Int, deviceId: String): Completable

    fun checkIfHasStarted(userId: Int, taskId: Int): Flowable<Int>
}
