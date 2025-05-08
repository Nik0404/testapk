package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.Task
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TaskDao {

    @Query("UPDATE task SET unanswered_checklist = :unansweredCheckLists WHERE id = :taskId")
    fun updateTaskUnansweredCheckLists(taskId: Int, unansweredCheckLists: Int): Completable

    @Query("UPDATE task SET user_comment = :comment WHERE id = :taskId")
    fun updateTaskComment(taskId: Int, comment: String): Completable

    @Query("UPDATE task SET task_status_code = :status WHERE id = :taskId")
    fun updateTaskStatus(taskId: Int, status: Int): Completable

    @Query("UPDATE task SET start_date_fact = :date WHERE id = :taskId AND (start_date_fact IS NULL OR start_date_fact = '')")
    fun updateTaskActualStartDate(taskId: Int, date: String): Completable

    @Query("UPDATE task SET end_date_fact = :date WHERE id = :taskId")
    fun updateTaskActualEndDate(taskId: Int, date: String): Completable

    @Query("UPDATE task SET notification_date = :date WHERE id = :taskId")
    fun updateTaskNotificationDate(taskId: Int, date: String): Completable

    @Query("UPDATE task SET executor_id = :executorId WHERE id = :taskId")
    fun updateTaskExecutor(taskId: Int, executorId: Int): Completable

    @Query("UPDATE task SET device_id_on_start = :deviceId WHERE id = :taskId")
    fun updateTaskStartDevice(taskId: Int, deviceId: String): Completable

    @Query("UPDATE task SET device_id_on_finish = :deviceId WHERE id = :taskId")
    fun updateTaskFinishDevice(taskId: Int, deviceId: String): Completable

    @Query("Select count(*) from task WHERE task.executor_id = :userId and task_status_code=20 and task.id != :taskId")
    fun checkIfHasStarted(userId: Int, taskId: Int): Flowable<Int>


    @Query(
        """
        SELECT
            task.id AS id,
            task.task_number AS number,
            task.task_name as name,
            task.start_date_plan AS startDatePlanned,
            task.end_date_plan AS endDatePlanned,
            task.start_date_fact AS startDateActual,
            task.end_date_fact AS endDateActual,
            task.task_status_code AS status,
            task.user_comment AS comment,
            task.notification_date AS notificationDate,
            task.executor_group_id AS executorGroupId,
            task.executor_id AS executorId,
            task.device_id_on_start AS deviceIdOnStart,
            task.device_id_on_start AS deviceIdOnFinish,
            (
            SELECT sum(route.count_question) 
                FROM route 
            WHERE route.task_id = task.id
            ) AS checkLists,
            (
            SELECT sum(route.count_answer) 
                FROM route 
            WHERE route.task_id = task.id
            ) AS unansweredCheckLists,
            (
            SELECT count(*)
                FROM route
                    LEFT OUTER JOIN check_list
                        ON check_list.route_id = route.id
                WHERE check_list.is_defect = 1
                AND route.task_id = task.id
            ) AS defectsCount,
            (
            SELECT count(*)
                FROM media_file
                    LEFT JOIN check_list, route
                        ON media_file.check_list_id = check_list.id
                WHERE check_list.route_id = route.id
                AND route.task_id = task.id
            ) AS attachmentsCount,
            (
            SELECT short_name
                FROM executor
                WHERE executor.executor_id = task.executor_id
            ) AS executorName
        FROM task
        WHERE task.task_status_code IN (:statusCodes)
        ORDER BY task.id DESC
    """
    )
    fun getTasksByStatusCodes(statusCodes: ArrayList<Int>): Flowable<List<DisplayTask>>

    @Query(
        """
        SELECT
            task.id AS id,
            task.task_number AS number,
            task.task_name as name,
            task.start_date_plan AS startDatePlanned,
            task.end_date_plan AS endDatePlanned,
            task.start_date_fact AS startDateActual,
            task.end_date_fact AS endDateActual,
            task.task_status_code AS status,
            task.user_comment AS comment,
            task.notification_date AS notificationDate,
            task.executor_group_id AS executorGroupId,
            task.executor_id AS executorId,
            task.device_id_on_start AS deviceIdOnStart,
            task.device_id_on_start AS deviceIdOnFinish,
            (
            SELECT sum(route.count_question) 
                FROM route 
            WHERE route.task_id = task.id
            ) AS checkLists,
            (
            SELECT sum(route.count_answer) 
                FROM route 
            WHERE route.task_id = task.id
            ) AS unansweredCheckLists,
            (
            SELECT count(*)
                FROM route
                    LEFT OUTER JOIN check_list
                        ON check_list.route_id = route.id
                WHERE check_list.is_defect = 1
                AND route.task_id = task.id
            ) AS defectsCount,
            (
            SELECT count(*)
                FROM media_file
                    LEFT JOIN check_list, route
                        ON media_file.check_list_id = check_list.id
                WHERE check_list.route_id = route.id
                AND route.task_id = task.id
            ) AS attachmentsCount,
            (
            SELECT short_name
                FROM executor
                WHERE executor.executor_id = task.executor_id
            ) AS executorName
        FROM task
        WHERE task.task_status_code IN (:statusCodes)
            AND task.executor_id = :executorId
        ORDER BY task.id DESC
    """
    )
    fun getTasksByExecutorId(
        statusCodes: ArrayList<Int>,
        executorId: Int
    ): Flowable<List<DisplayTask>>

    //                task.task_status_code
    @Query(
        """
        SELECT
            task.id AS id,
            task.task_number AS number,
            task.task_name as name,
            task.start_date_plan AS startDatePlanned,
            task.end_date_plan AS endDatePlanned,
            task.start_date_fact AS startDateActual,
            task.end_date_fact AS endDateActual,
            task.task_status_code AS status,
            task.user_comment AS comment,
            task.notification_date AS notificationDate,
            task.executor_group_id AS executorGroupId,
            task.executor_id AS executorId,
            task.device_id_on_start AS deviceIdOnStart,
            task.device_id_on_start AS deviceIdOnFinish,
            (
            SELECT sum(route.count_question) 
                FROM route 
            WHERE route.task_id = task.id
            ) AS checkLists,
            (
            SELECT sum(route.count_answer) 
                FROM route 
            WHERE route.task_id = task.id
            ) AS unansweredCheckLists,
            (
            SELECT count(*)
                FROM route
                    LEFT OUTER JOIN check_list
                        ON check_list.route_id = route.id
                WHERE check_list.is_defect = 1
                AND route.task_id = task.id
            ) AS defectsCount,
            (
            SELECT count(*)
                FROM media_file
                    LEFT JOIN check_list, route
                        ON media_file.check_list_id = check_list.id
                WHERE check_list.route_id = route.id
                AND route.task_id = task.id
            ) AS attachmentsCount,
            (
            SELECT short_name
                FROM executor
                WHERE executor.executor_id = task.executor_id
            ) AS executorName
        FROM task
        WHERE task.id = :id
    """
    )
    fun getTaskById(id: Int): Flowable<DisplayTask>

    @Query("SELECT * FROM task WHERE task.task_status_code = :statusCode")
    fun getTasksForChangeExecutors(statusCode: Int): Single<List<Task>>

    @Query("SELECT executor_id FROM task WHERE id = :taskId")
    fun getExecutorIdByTaskId(taskId: Int): Flowable<Int>

    @Query(
        """
        SELECT executor_id 
        FROM task
            LEFT JOIN route
                ON route.task_id = task.id
        WHERE route.id = :routeId
    """
    )
    fun getExecutorIdByRouteId(routeId: Int): Single<Int>

    @Query("DELETE FROM task WHERE task.id = :taskId")
    fun deleteTaskById(taskId: Int): Completable

    @Query("SELECT task.id FROM task WHERE task.id = :taskId")
    fun checkIfTaskExists(taskId: Int): Maybe<Int>
}
