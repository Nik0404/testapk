package com.acroninspector.app.domain.interactors.nfc.scan

import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import io.reactivex.Completable
import io.reactivex.Maybe

interface ScanNfcInteractor {

    fun getRouteIdByNfcCode(nfcCode: String): Maybe<List<Int>>

    fun getRouteByNfcCode(taskId: Int, nfcCode: String): Maybe<DisplayRoute>

    fun getEquipmentIdByNfcCode(nfcCode: String): Maybe<Int>

    fun updateNfcTime(routeId: Int, nfcCode: String, nfcTime: String, taskStatus: Int): Completable
}