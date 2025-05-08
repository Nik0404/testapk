package com.acroninspector.app.domain.interactors.nfc

import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import com.acroninspector.app.domain.repositories.NfcRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import io.reactivex.Completable
import io.reactivex.Single

class NfcInteractorImpl(
        private val nfcRepository: NfcRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : NfcInteractor {

    override fun checkIfNfcExists(nfcCode: String): Single<Int> {
        return nfcRepository.checkIfNfcExists(nfcCode)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.io())
                .flatMap { isExists ->
                    if (isExists) {
                        Single.just(DEFAULT_INVALID_ID)
                    } else nfcRepository.getMaxEquipmentNfcTagId()
                }.subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun saveNfcEquipment(nfcEquipment: NfcEquipment): Completable {
        return nfcRepository.insertNfcMark(nfcEquipment)
                .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}