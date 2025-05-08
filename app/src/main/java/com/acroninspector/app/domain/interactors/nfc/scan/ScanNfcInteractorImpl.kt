package com.acroninspector.app.domain.interactors.nfc.scan

import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.repositories.NfcRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.RouteRepository
import io.reactivex.Completable
import io.reactivex.Maybe

class ScanNfcInteractorImpl(
        private val nfcRepository: NfcRepository,
        private val routeRepository: RouteRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : ScanNfcInteractor {

    override fun getRouteByNfcCode(taskId: Int, nfcCode: String): Maybe<DisplayRoute> {
        return nfcRepository.getRouteIdByNfcCode(taskId, nfcCode)
                .flatMap { routeRepository.getRouteById(it).firstElement() }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getRouteIdByNfcCode(nfcCode: String): Maybe<List<Int>> {
        return nfcRepository.getRouteIdByNfcCode(nfcCode)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getEquipmentIdByNfcCode(nfcCode: String): Maybe<Int> {
        return nfcRepository.getEquipmentIdByNfcCode(nfcCode)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun updateNfcTime(routeId: Int, nfcCode: String, nfcTime: String, taskStatus: Int): Completable {
        return nfcRepository.getNfcRouteByNfcCode(routeId, nfcCode)
                .subscribeOn(schedulersProvider.io())
                .flatMapCompletable { nfcRoute ->
                    if (nfcRoute.time.isEmpty() && taskStatus == DatabaseConstants.TASK_STATUS_IN_PROGRESS) {
                        nfcRepository.updateNfcTime(routeId, nfcCode, nfcTime)
                    } else Completable.complete()
                }.doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .observeOn(schedulersProvider.ui())
    }
}