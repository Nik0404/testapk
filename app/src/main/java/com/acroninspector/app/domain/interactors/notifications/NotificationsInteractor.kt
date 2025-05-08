package com.acroninspector.app.domain.interactors.notifications

import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface NotificationsInteractor {

    fun getExecutorId(): Int

    fun getUnreadedNotifications(): Flowable<List<DisplayNotification>>

    fun getReadedNotifications(): Flowable<List<DisplayNotification>>

    fun readNotification(notificationId: Int, dateOfReading: String): Single<DisplayTask>

    fun getTaskById(taskId: Int): Single<DisplayTask>

    fun deleteNotification(notificationId: Int): Completable
}