package com.acroninspector.app.domain.interactors.defectname

import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import io.reactivex.Flowable
import io.reactivex.Single

interface DefectNameInteractor {

    fun getAllDefectNames(equipmentClassId: Int): Single<List<DisplayDefect>>

    fun getSearchedDefectNames(query: String, equipmentClassId: Int): Flowable<List<DisplayDefect>>
}