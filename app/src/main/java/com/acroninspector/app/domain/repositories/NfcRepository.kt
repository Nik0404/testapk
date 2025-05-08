package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import com.acroninspector.app.domain.entity.local.database.NfcRoute
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface NfcRepository {

    fun getNfcMarksByEquipmentId(equipmentId: Int): Flowable<List<NfcEquipment>>

    fun getNfcRouteByNfcCode(routeId: Int, nfcCode: String): Single<NfcRoute>

    fun getNfcNameByNfcCode(nfcCode: String): Single<String>

    fun getEquipmentIdByNfcCode(nfcCode: String): Maybe<Int>

    fun getRouteIdByNfcCode(nfcCode: String): Maybe<List<Int>>

    fun getRouteIdByNfcCode(taskId: Int, nfcCode: String): Maybe<Int>

    fun checkIfNfcExists(nfcCode: String): Single<Boolean>

    fun getUnansweredNfcMarks(routes: List<DisplayRoute>): Single<String>

    fun getMaxEquipmentNfcTagId(): Single<Int>

    fun updateNfcTime(routeId: Int, nfcCode: String, nfcTime: String): Completable

    fun insertNfcMark(nfcEquipment: NfcEquipment): Completable
}
