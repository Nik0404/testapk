package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.LocalDefectDao
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class LocalDefectRepositoryImpl(
        private val localDefectDao: LocalDefectDao
) : LocalDefectRepository {

    override fun getLocalDefects(): Flowable<List<DisplayDefectLog>> {
        return localDefectDao.getDisplayLocalDefects()
    }

    override fun getLocalDefectById(localDefectId: Int): Single<LocalDefect> {
        return localDefectDao.getLocalDefectById(localDefectId)
    }

    override fun getLocalDefectByCheckListId(checkListId: Int): Maybe<LocalDefect> {
        return localDefectDao.getLocalDefectByCheckListId(checkListId)
    }

    override fun getMaxLocalDefectId(): Single<Int> {
        return localDefectDao.getMaxLocalDefectId()
    }

    override fun getDisplayDefectById(localDefectId: Int): Flowable<DisplayDefectLog> {
        return localDefectDao.getDisplayLocalDefectById(localDefectId)
    }

    override fun getLocalDefectsByEquipmentId(equipmentId: Int): Flowable<List<DisplayDefectLog>> {
        return localDefectDao.getDisplayLocalDefectsByEquipmentId(equipmentId)
    }

    override fun getLocalDefectsByEquipmentIdList(equipmentId: String): Flowable<List<DisplayDefectLog>> {
        val list = equipmentId.split(",").map { it.toInt() }
        return localDefectDao.getDisplayLocalDefectsByEquipmentIdList(list)
            .map { flowList -> flowList.map { it.toDisplayDefectLog() } }
    }

    override fun getLocalDefectsByRouteId(routeId: Int): Flowable<List<DisplayDefectLog>> {
        return localDefectDao.getDisplayLocalDefectsByRouteId(routeId)
    }

    override fun getExtraLocalDefectsByCompletedTaskId(taskId: Int): Single<List<LocalDefect>> {
        return localDefectDao.getExtraLocalDefectsByCompletedTaskId(taskId)
    }

    override fun getDisplayDefectLogByCheckListId(checkListId: Int): Maybe<DisplayDefectLog> {
        return localDefectDao.getDisplayDefectByCheckListId(checkListId)
    }

    override fun getDefectAttachmentsCount(localDefectId: Int): Flowable<Int> {
        return localDefectDao.getDisplayDefectAttachmentsCountByDefectId(localDefectId)
    }

    override fun getDefectComment(localDefectId: Int): Flowable<String> {
        return localDefectDao.getDefectCommentByDefectId(localDefectId)
    }

    override fun getDefectsCount(routeId: Int): Flowable<Int> {
        return localDefectDao.getDefectsCount(routeId)
    }

    override fun insertLocalDefect(localDefect: LocalDefect): Completable {
        return localDefectDao.insertLocalDefect(localDefect)
    }

    override fun updateComment(comment: String, localDefectId: Int): Completable {
        return localDefectDao.updateComment(comment, localDefectId)
    }

    override fun updateCommentByCheckListId(comment: String, checkListId: Int): Completable {
        return localDefectDao.updateCommentByCheckListId(comment, checkListId)
    }

    override fun deleteDefect(localDefectId: Int): Completable {
        return localDefectDao.deleteLocalDefectById(localDefectId)
    }

    override fun deleteLocalDefects(localDefects: List<LocalDefect>): Completable {
        return localDefectDao.deleteLocalDefects(localDefects)
    }

    override fun checkIfDefectLogExists(defectLogId: Int): Maybe<Int> {
        return localDefectDao.checkIfDefectLogExists(defectLogId)
    }
}