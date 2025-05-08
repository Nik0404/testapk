package com.acroninspector.app.domain.interactors.taskcomment

import com.acroninspector.app.domain.entity.local.display.DisplayTask
import io.reactivex.Completable
import io.reactivex.Flowable

interface TaskCommentInteractor {

    fun saveTaskComment(taskId: Int, comment: String): Completable

    fun getTaskById(taskId: Int): Flowable<DisplayTask>
}