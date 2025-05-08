package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.Executor
import com.acroninspector.app.domain.entity.local.display.DisplayExecutor
import io.reactivex.Single

interface ExecutorRepository {

    fun getAllExecutors(): Single<List<DisplayExecutor>>

    fun getExecutorsByGroup(executorGroup: Int): Single<List<DisplayExecutor>>

    fun getExecutorById(id: Int): Single<Executor>

    fun getExecutorsByFullName(fullName: String): Single<List<Executor>>
}