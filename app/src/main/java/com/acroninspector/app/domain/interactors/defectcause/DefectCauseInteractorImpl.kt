package com.acroninspector.app.domain.interactors.defectcause

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import com.acroninspector.app.domain.repositories.DefectCauseRepository
import io.reactivex.Single

class DefectCauseInteractorImpl(
        private val defectCauseRepository: DefectCauseRepository,
        private val schedulersProvider: SchedulersProvider
) : DefectCauseInteractor {

    override fun getAllDefectCauses(defectNameId: Int, equipmentClassId: Int): Single<List<DisplayDefectCause>> {
        return defectCauseRepository.getAllDefectCauses(defectNameId, equipmentClassId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}