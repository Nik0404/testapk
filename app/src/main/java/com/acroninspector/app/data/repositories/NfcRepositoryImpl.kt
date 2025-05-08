package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.NfcEquipmentDao
import com.acroninspector.app.data.datasource.database.dao.NfcRouteDao
import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import com.acroninspector.app.domain.entity.local.database.NfcRoute
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.repositories.NfcRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.lang.Exception

class NfcRepositoryImpl(
        private val nfcRouteDao: NfcRouteDao,
        private val nfcEquipmentDao: NfcEquipmentDao
) : NfcRepository {

    override fun getNfcMarksByEquipmentId(equipmentId: Int): Flowable<List<NfcEquipment>> {
        return nfcEquipmentDao.getNfcMarksByEquipmentId(equipmentId)
    }

    override fun getNfcRouteByNfcCode(routeId: Int, nfcCode: String): Single<NfcRoute> {
        return nfcRouteDao.getNfcRouteByNfcCode(routeId, nfcCode)
    }

    override fun getNfcNameByNfcCode(nfcCode: String): Single<String> {
        return nfcRouteDao.getNfcNameByNfcCode(nfcCode)
    }

    override fun getEquipmentIdByNfcCode(nfcCode: String): Maybe<Int> {
        return nfcEquipmentDao.getEquipmentIdByNfcCode(nfcCode)
    }

    override fun getRouteIdByNfcCode(nfcCode: String): Maybe<List<Int>> {
        return nfcRouteDao.getRouteIdByNfcCode(nfcCode)
    }

    override fun getRouteIdByNfcCode(taskId: Int, nfcCode: String): Maybe<Int> {
        return nfcRouteDao.getRouteIdByNfcCode(taskId, nfcCode)
    }

    override fun checkIfNfcExists(nfcCode: String): Single<Boolean> {
        return Single.create { emitter ->
            val nfcMarks = nfcEquipmentDao.getNfcEquipmentMarks()

            var nfcExists = false
            for (nfcMark in nfcMarks) {
                if (nfcMark.code == nfcCode) {
                    nfcExists = true
                    break
                }
            }
            emitter.onSuccess(nfcExists)
        }
    }

    override fun getUnansweredNfcMarks(routes: List<DisplayRoute>): Single<String> {
        return Single.create<String> { emitter ->
            val unansweredNfcMarks = StringBuilder()

            for (route in routes) {
                val nfcMarks = nfcRouteDao.getNfcRouteMarksByRouteId(route.id)
                nfcMarks.forEach { nfcRoute ->
                    if (nfcRoute.time.isEmpty()) {
                        unansweredNfcMarks.append(nfcRoute.name).append(' ')
                            .append('(').append(nfcRoute.code).append(')')
                            .append('\n')
                    }
                }
            }

            val result = try {
                unansweredNfcMarks.trim()
            } catch (e: Exception) {
                ""
            }

            emitter.onSuccess(result.toString())
        }
    }

    override fun getMaxEquipmentNfcTagId(): Single<Int> {
        return nfcEquipmentDao.getMaxEquipmentNfcTagId()
    }

    override fun updateNfcTime(routeId: Int, nfcCode: String, nfcTime: String): Completable {
        return nfcRouteDao.updateNfcTime(routeId, nfcCode, nfcTime)
    }

    override fun insertNfcMark(nfcEquipment: NfcEquipment): Completable {
        return nfcEquipmentDao.insertNfcMark(nfcEquipment)
    }
}
