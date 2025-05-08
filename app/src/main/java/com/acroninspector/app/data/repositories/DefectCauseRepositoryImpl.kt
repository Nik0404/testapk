package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.DefectCauseDao
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import com.acroninspector.app.domain.repositories.DefectCauseRepository
import io.reactivex.Single

class DefectCauseRepositoryImpl(
        private val defectCauseDao: DefectCauseDao
) : DefectCauseRepository {

    override fun getAllDefectCauses(defectNameId: Int, equipmentClassId: Int): Single<List<DisplayDefectCause>> {
        return defectCauseDao.getAllDefectCauses(defectNameId, equipmentClassId).firstOrError()
    }

    override fun getDefectCauseById(defectCauseId: Int): Single<DisplayDefectCause> {
        return defectCauseDao.getDefectCauseById(defectCauseId)
    }
}