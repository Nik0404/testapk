package com.acroninspector.app.domain.interactors.controlprocedure

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.RouteRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class ControlProcedureInteractorImpl(
        private val routeRepository: RouteRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : ControlProcedureInteractor {

    override fun getControlProceduresSortedByNumber(taskId: Int): Single<List<DisplayControlProcedure>> {
        return routeRepository.getControlProceduresByTaskId(taskId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun replaceControlProcedures(displayControlProcedures: List<DisplayControlProcedure>): Completable {
        return Observable.just(displayControlProcedures)
                .flatMapIterable { it }
                .flatMapCompletable { controlProcedure ->
                    routeRepository.updateRoute(controlProcedure.orderNumber, controlProcedure.id)
                }.doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}