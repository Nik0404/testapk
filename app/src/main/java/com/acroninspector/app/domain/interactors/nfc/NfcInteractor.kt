package com.acroninspector.app.domain.interactors.nfc

import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import io.reactivex.Completable
import io.reactivex.Single

interface NfcInteractor {

    fun checkIfNfcExists(nfcCode: String): Single<Int>

    fun saveNfcEquipment(nfcEquipment: NfcEquipment): Completable
}