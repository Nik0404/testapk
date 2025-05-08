package com.acroninspector.app.data.repositories

import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.data.datasource.database.dao.TaskDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.util.constants.TaskColumns
import com.acroninspector.app.data.util.filters.TaskFilter
import com.acroninspector.app.data.util.mappers.TaskMapper
import com.acroninspector.app.domain.entity.local.database.Task
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.entity.remote.request.GettingDataRequest
import com.acroninspector.app.domain.entity.remote.request.other.Order
import com.acroninspector.app.domain.repositories.TaskRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class TaskRepositoryImpl(
        private val functionsApi: FunctionsApi,
        private val taskDao: TaskDao,
        private val taskMapper: TaskMapper
) : TaskRepository {

    override fun getCompletedTasksFromNetwork(sessionId: String, functionId: Int,
                                              executorGroupIds: List<Int>, divisionId: Int): Flowable<List<Task>> {
        val order = listOf(Order(
                TaskColumns.TASK_ID_COLUMN_NAME,
                NetworkConstants.ORDER_DESC
        ))
        val requestBody = GettingDataRequest(
                sessionId = sessionId,
                functionId = functionId,
                functionName = FunctionNames.TASKS,
                columns = TaskColumns.getColumns(),
                filter = TaskFilter.getFilterCompletedTasks(executorGroupIds, divisionId),
                order = order
        )

        return functionsApi.getData(requestBody)
                .map { response ->
                    val tasks = mutableListOf<Task>()

                    response.values.forEach {
                        val taskItem = taskMapper.fromSchemaToEntity(it)
                        tasks.add(taskItem as Task)
                    }

                    tasks.toList()
                }.toFlowable()
    }

    override fun getTasksByStatusCodes(statusCode: ArrayList<Int>): Flowable<List<DisplayTask>> {
        return taskDao.getTasksByStatusCodes(statusCode)
    }

    override fun getTaskById(id: Int): Flowable<DisplayTask> {
        return taskDao.getTaskById(id)
    }

    override fun getExecutorIdByTaskId(id: Int): Flowable<Int> {
        return taskDao.getExecutorIdByTaskId(id)
    }

    override fun updateTaskComment(taskId: Int, comment: String): Completable {
        return taskDao.updateTaskComment(taskId, comment)
    }

    override fun updateTaskStatus(taskId: Int, status: Int): Completable {
        return taskDao.updateTaskStatus(taskId, status)
    }

    override fun updateTaskActualStartDate(taskId: Int, actualStartDate: String): Completable {
        return taskDao.updateTaskActualStartDate(taskId, actualStartDate)
    }

    override fun updateTaskActualEndDate(taskId: Int, actualEndDate: String): Completable {
        return taskDao.updateTaskActualEndDate(taskId, actualEndDate)
    }

    override fun updateTaskExecutor(taskId: Int, executorId: Int): Completable {
        return taskDao.updateTaskExecutor(taskId, executorId)
    }

    override fun updateNotificationDate(taskId: Int, notificationDate: String): Completable {
        return taskDao.updateTaskNotificationDate(taskId, notificationDate)
    }

    override fun checkIfTaskExists(taskId: Int): Maybe<Int> {
        return taskDao.checkIfTaskExists(taskId)
    }

    override fun updateTaskStartDevice(taskId: Int, deviceId: String): Completable {
        return taskDao.updateTaskStartDevice(taskId, deviceId)
    }

    override fun updateTaskFinishDevice(taskId: Int, deviceId: String): Completable {
        return taskDao.updateTaskFinishDevice(taskId, deviceId)
    }

    override fun checkIfHasStarted(userId: Int, taskId: Int): Flowable<Int> {
        return taskDao.checkIfHasStarted(userId, taskId)
    }

}
