package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import io.reactivex.Completable
import io.reactivex.Flowable

interface NotificationsRepository {

    fun getUnreadedNotifications(): Flowable<List<DisplayNotification>>

    fun getReadedNotifications(): Flowable<List<DisplayNotification>>

    fun readNotification(notificationId: Int, dateOfReading: String): Completable

    fun deleteNotification(notificationId: Int): Completable
}