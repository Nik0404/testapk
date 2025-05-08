package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface NotificationsDao {

    @Query("""
        SELECT 
            notification.task_id AS taskId,
            notification.date_of_reading AS dateOfReading,
            notification.date_creation AS dateCreation,
            (
            SELECT task.task_status_code
                FROM task
                WHERE task.id = task_id
            ) AS taskStatus,
            (
            SELECT task.task_number
                FROM task
                WHERE task.id = task_id
            ) AS taskNumber,
            (
            SELECT task.task_name
                FROM task
                WHERE task.id = task_id
            ) AS taskName,
            (
            SELECT task.executor_id
                FROM task
                WHERE task.id = task_id
            ) AS executorId,
            (
            SELECT task.start_date_plan
                FROM task
                WHERE task.id = task_id
            ) AS taskStartDatePlanned,
            (
            SELECT task.end_date_plan
                FROM task
                WHERE task.id = task_id
            ) AS taskEndDatePlanned,
            (
            SELECT executor.short_name
                FROM executor
                    LEFT JOIN task
                        ON task.executor_id = executor.executor_id
                WHERE task.id = notification.task_id
            ) AS executorName
        FROM notification 
        WHERE date_of_reading = ''
        ORDER BY task_id DESC
    """)
    fun getUnreadedNotifications(): Flowable<List<DisplayNotification>>

    @Query("""
        SELECT 
            notification.task_id AS taskId,
            notification.date_of_reading AS dateOfReading,
            notification.date_creation AS dateCreation,
            (
            SELECT task.task_status_code
                FROM task
                WHERE task.id = task_id
            ) AS taskStatus,
            (
            SELECT task.task_number
                FROM task
                WHERE task.id = task_id
            ) AS taskNumber,
            (
            SELECT task.task_name
                FROM task
                WHERE task.id = task_id
            ) AS taskName,
            (
            SELECT task.executor_id
                FROM task
                WHERE task.id = task_id
            ) AS executorId,
            (
            SELECT task.start_date_plan
                FROM task
                WHERE task.id = task_id
            ) AS taskStartDatePlanned,
            (
            SELECT task.end_date_plan
                FROM task
                WHERE task.id = task_id
            ) AS taskEndDatePlanned,
            (
            SELECT executor.short_name
                FROM executor
                    LEFT JOIN task
                        ON task.executor_id = executor.executor_id
                WHERE task.id = notification.task_id
            ) AS executorName
        FROM notification 
        WHERE date_of_reading <> ''
        ORDER BY task_id DESC
    """)
    fun getReadedNotifications(): Flowable<List<DisplayNotification>>

    @Query("UPDATE notification SET date_of_reading = :dateOfReading WHERE task_id = :id")
    fun readNotification(id: Int, dateOfReading: String): Completable

    @Query("DELETE FROM notification WHERE task_id = :id")
    fun deleteNotification(id: Int): Completable
}
