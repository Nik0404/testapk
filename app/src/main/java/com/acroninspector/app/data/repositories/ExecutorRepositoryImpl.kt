package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.ExecutorDao
import com.acroninspector.app.domain.entity.local.database.Executor
import com.acroninspector.app.domain.entity.local.display.DisplayExecutor
import com.acroninspector.app.domain.repositories.ExecutorRepository
import io.reactivex.Single

class ExecutorRepositoryImpl(
        private val executorDao: ExecutorDao
) : ExecutorRepository {

    override fun getAllExecutors(): Single<List<DisplayExecutor>> {
        return executorDao.getAllExecutors()
    }

    override fun getExecutorsByGroup(executorGroup: Int): Single<List<DisplayExecutor>> {
        return executorDao.getExecutorsByGroup(executorGroup)
    }

    override fun getExecutorById(id: Int): Single<Executor> {
        return executorDao.getExecutorById(id)
    }

    override fun getExecutorsByFullName(fullName: String): Single<List<Executor>> {
        return executorDao.getExecutorsByFullName(fullName)
    }
}