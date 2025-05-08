package com.acroninspector.app.domain.interactors.executor

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayExecutor
import com.acroninspector.app.domain.repositories.ExecutorRepository
import io.reactivex.Single

class ExecutorInteractorImpl(
        private val executorRepository: ExecutorRepository,
        private val schedulersProvider: SchedulersProvider
) : ExecutorInteractor {

    override fun getExecutorsByGroup(executorGroup: Int): Single<List<DisplayExecutor>> {
        return executorRepository.getExecutorsByGroup(executorGroup)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getExecutorById(id: Int): Single<DisplayExecutor> {
        return executorRepository
                .getExecutorById(id)
                .observeOn(schedulersProvider.io())
                .map { DisplayExecutor(it.id, it.fullName, it.shortName, it.groupNumber, it.groupName) }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}