package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.NotificationsDao
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.acroninspector.app.domain.repositories.NotificationsRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class NotificationsRepositoryImpl(
        private val notificationsDao: NotificationsDao
) : NotificationsRepository {

    override fun getUnreadedNotifications(): Flowable<List<DisplayNotification>> {
        return notificationsDao.getUnreadedNotifications()
    }

    override fun getReadedNotifications(): Flowable<List<DisplayNotification>> {
        return notificationsDao.getReadedNotifications()
    }

    override fun readNotification(notificationId: Int, dateOfReading: String): Completable {
        return notificationsDao.readNotification(notificationId, dateOfReading)
    }

    override fun deleteNotification(notificationId: Int): Completable {
        return notificationsDao.deleteNotification(notificationId)
    }
}