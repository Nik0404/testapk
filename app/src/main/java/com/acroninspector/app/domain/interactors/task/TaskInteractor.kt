package com.acroninspector.app.domain.interactors.task

import com.acroninspector.app.domain.entity.local.display.DisplayTask
import io.reactivex.Flowable

interface TaskInteractor {

    fun getCompletedTasks(): Flowable<List<DisplayTask>>

    fun getTasksByStatusCodes(statusCode: ArrayList<Int>): Flowable<List<DisplayTask>>
}
