package com.acroninspector.app.domain.interactors.defectname

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.domain.repositories.DefectRepository
import io.reactivex.Flowable
import io.reactivex.Single

class DefectNameInteractorImpl(
        private val defectNameRepository: DefectRepository,
        private val schedulersProvider: SchedulersProvider
) : DefectNameInteractor {

    override fun getAllDefectNames(equipmentClassId: Int): Single<List<DisplayDefect>> {
        return defectNameRepository.getAllDefects(equipmentClassId)
                .map { it.distinctBy { defect -> defect.fullName } }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getSearchedDefectNames(query: String, equipmentClassId: Int): Flowable<List<DisplayDefect>> {
        return defectNameRepository.getSearchedDefects(query, equipmentClassId)
                .map { it.distinctBy { defect -> defect.fullName } }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}