package com.acroninspector.app.domain.interactors.executor

import com.acroninspector.app.domain.entity.local.display.DisplayExecutor
import io.reactivex.Single

interface ExecutorInteractor {

    fun getExecutorById(id: Int): Single<DisplayExecutor>

    fun getExecutorsByGroup(executorGroup: Int): Single<List<DisplayExecutor>>
}