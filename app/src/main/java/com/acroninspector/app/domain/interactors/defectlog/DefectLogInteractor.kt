package com.acroninspector.app.domain.interactors.defectlog

import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import io.reactivex.Flowable

interface DefectLogInteractor {

    fun getAllDefectLogs(): Flowable<List<DisplayDefectLog>>

    fun getSearchedDefectLogs(searchQuery: String): Flowable<List<DisplayDefectLog>>

    fun getDefectLogsByEquipmentId(equipmentId: Int): Flowable<List<DisplayDefectLog>>

    fun getDefectLogsByEquipmentIdList(equipmentIdList: String): Flowable<List<DisplayDefectLog>>

    fun getDefectLogsByRouteId(routeId: Int): Flowable<List<DisplayDefectLog>>

}
