package com.acroninspector.app.domain.interactors.registerdefect

import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.database.Equipment
import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface RegisterDefectInteractor {

    fun getLocalDefectById(localDefectId: Int): Single<LocalDefect>

    fun insertDefect(localDefect: LocalDefect): Completable

    fun deleteDefect(localDefectId: Int): Completable

    fun deleteMediaFilesByDefectLogId(localDefectId: Int): Completable

    fun getDefectCauseById(defectCauseId: Int): Single<DisplayDefectCause>

    fun getDefectNameById(defectNameId: Int): Single<DisplayDefect>

    fun getEquipmentById(equipmentId: Int): Single<Equipment>

    fun getDefectAttachmentsCount(localDefectId: Int): Flowable<Int>

    fun getCheckListAttachmentsCount(checkListId: Int): Flowable<Int>

    fun getLocalDefectId(): Single<Int>
}