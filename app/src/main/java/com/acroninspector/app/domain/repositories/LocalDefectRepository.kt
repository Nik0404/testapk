package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface LocalDefectRepository {

    fun getMaxLocalDefectId(): Single<Int>

    fun getLocalDefectById(localDefectId: Int): Single<LocalDefect>

    fun getLocalDefects(): Flowable<List<DisplayDefectLog>>

    fun getDisplayDefectById(localDefectId: Int): Flowable<DisplayDefectLog>

    fun getLocalDefectsByEquipmentId(equipmentId: Int): Flowable<List<DisplayDefectLog>>

    fun getLocalDefectsByEquipmentIdList(equipmentId: String): Flowable<List<DisplayDefectLog>>

    fun getLocalDefectsByRouteId(routeId: Int): Flowable<List<DisplayDefectLog>>

    fun getDisplayDefectLogByCheckListId(checkListId: Int): Maybe<DisplayDefectLog>

    fun getLocalDefectByCheckListId(checkListId: Int): Maybe<LocalDefect>

    fun getExtraLocalDefectsByCompletedTaskId(taskId: Int): Single<List<LocalDefect>>

    fun getDefectAttachmentsCount(localDefectId: Int): Flowable<Int>

    fun getDefectComment(localDefectId: Int): Flowable<String>

    fun getDefectsCount(routeId: Int): Flowable<Int>

    fun insertLocalDefect(localDefect: LocalDefect): Completable

    fun updateComment(comment: String, localDefectId: Int): Completable

    fun updateCommentByCheckListId(comment: String, checkListId: Int): Completable

    fun deleteDefect(localDefectId: Int): Completable

    fun deleteLocalDefects(localDefects: List<LocalDefect>): Completable

    fun checkIfDefectLogExists(defectLogId: Int): Maybe<Int>
}