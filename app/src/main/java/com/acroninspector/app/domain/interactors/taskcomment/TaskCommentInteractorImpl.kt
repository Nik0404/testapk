package com.acroninspector.app.domain.interactors.taskcomment

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class TaskCommentInteractorImpl(
        private val taskRepository: TaskRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : TaskCommentInteractor {

    override fun saveTaskComment(taskId: Int, comment: String): Completable {
        return taskRepository.updateTaskComment(taskId, comment)
                .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getTaskById(taskId: Int): Flowable<DisplayTask> {
        return taskRepository.getTaskById(taskId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}