package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.DefectDao
import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.domain.repositories.DefectRepository
import io.reactivex.Flowable
import io.reactivex.Single

class DefectRepositoryImpl(
        private val defectDao: DefectDao
) : DefectRepository {

    override fun getAllDefects(equipmentClassId: Int): Single<List<DisplayDefect>> {
        return defectDao.getAllDefects(equipmentClassId).firstOrError()
    }

    override fun getSearchedDefects(query: String, equipmentClassId: Int): Flowable<List<DisplayDefect>> {
        return if (query.isEmpty()) {
            defectDao.getAllDefects(equipmentClassId)
        } else defectDao.getSearchedDefects(query, equipmentClassId)
    }

    override fun getDefectById(defectId: Int): Single<DisplayDefect> {
        return defectDao.getDefectById(defectId)
    }
}