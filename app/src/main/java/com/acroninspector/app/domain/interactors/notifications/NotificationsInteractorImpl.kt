package com.acroninspector.app.domain.interactors.notifications

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.repositories.NotificationsRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class NotificationsInteractorImpl(
        private val notificationsRepository: NotificationsRepository,
        private val taskRepository: TaskRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : NotificationsInteractor {

    override fun getExecutorId(): Int {
        return preferencesRepository.executorId
    }

    override fun getReadedNotifications(): Flowable<List<DisplayNotification>> {
        return notificationsRepository.getReadedNotifications()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getUnreadedNotifications(): Flowable<List<DisplayNotification>> {
        return notificationsRepository.getUnreadedNotifications()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun readNotification(notificationId: Int, dateOfReading: String): Single<DisplayTask> {
        return Completable.concatArray(
                notificationsRepository.readNotification(notificationId, dateOfReading),
                taskRepository.updateNotificationDate(notificationId, dateOfReading)
        ).subscribeOn(schedulersProvider.io())
                .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .andThen(taskRepository.getTaskById(notificationId).firstOrError())
                .observeOn(schedulersProvider.ui())
    }

    override fun getTaskById(taskId: Int): Single<DisplayTask> {
        return taskRepository.getTaskById(taskId)
                .firstOrError()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun deleteNotification(notificationId: Int): Completable {
        return notificationsRepository.deleteNotification(notificationId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}