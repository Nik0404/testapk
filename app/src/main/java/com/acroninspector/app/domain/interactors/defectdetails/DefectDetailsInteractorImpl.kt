package com.acroninspector.app.domain.interactors.defectdetails

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import io.reactivex.Flowable

class DefectDetailsInteractorImpl(
        private val localDefectRepository: LocalDefectRepository,
        private val schedulersProvider: SchedulersProvider
) : DefectDetailsInteractor {

    override fun getDisplayDefectById(localDefectId: Int): Flowable<DisplayDefectLog> {
        return localDefectRepository.getDisplayDefectById(localDefectId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}