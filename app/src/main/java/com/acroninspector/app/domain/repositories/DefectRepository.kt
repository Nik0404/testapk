package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import io.reactivex.Flowable
import io.reactivex.Single

interface DefectRepository {

    fun getAllDefects(equipmentClassId: Int): Single<List<DisplayDefect>>

    fun getSearchedDefects(query: String, equipmentClassId: Int): Flowable<List<DisplayDefect>>

    fun getDefectById(defectId: Int): Single<DisplayDefect>
}