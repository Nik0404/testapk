package com.acroninspector.app.domain.interactors.defectdetails

import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import io.reactivex.Flowable

interface DefectDetailsInteractor {

    fun getDisplayDefectById(localDefectId: Int): Flowable<DisplayDefectLog>
}