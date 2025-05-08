package com.acroninspector.app.domain.interactors.defectcause

import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import io.reactivex.Single

interface DefectCauseInteractor {

    fun getAllDefectCauses(defectNameId: Int, equipmentClassId: Int): Single<List<DisplayDefectCause>>
}