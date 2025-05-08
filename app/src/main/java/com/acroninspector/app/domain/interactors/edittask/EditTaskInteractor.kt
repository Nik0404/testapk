package com.acroninspector.app.domain.interactors.edittask

import com.acroninspector.app.domain.entity.local.display.DisplayTask
import io.reactivex.Completable
import io.reactivex.Flowable

interface EditTaskInteractor {

    fun getTaskById(taskId: Int): Flowable<DisplayTask>

    fun getExecutorIdByTaskId(taskId: Int): Flowable<Int>

    fun updateTaskExecutor(taskId: Int, executorId: Int): Completable
}