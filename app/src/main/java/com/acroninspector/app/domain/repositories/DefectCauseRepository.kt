package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import io.reactivex.Single

interface DefectCauseRepository {

    fun getAllDefectCauses(defectNameId: Int, equipmentClassId: Int): Single<List<DisplayDefectCause>>

    fun getDefectCauseById(defectCauseId: Int): Single<DisplayDefectCause>
}