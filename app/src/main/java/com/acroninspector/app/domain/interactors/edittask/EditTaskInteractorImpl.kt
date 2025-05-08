package com.acroninspector.app.domain.interactors.edittask

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.TaskRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class EditTaskInteractorImpl(
        private val taskRepository: TaskRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : EditTaskInteractor {

    override fun getTaskById(taskId: Int): Flowable<DisplayTask> {
        return taskRepository.getTaskById(taskId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getExecutorIdByTaskId(taskId: Int): Flowable<Int> {
        return taskRepository.getExecutorIdByTaskId(taskId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun updateTaskExecutor(taskId: Int, executorId: Int): Completable {
        return taskRepository.updateTaskExecutor(taskId, executorId)
                .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}