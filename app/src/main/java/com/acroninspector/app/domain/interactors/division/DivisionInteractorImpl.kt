package com.acroninspector.app.domain.interactors.division

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.repositories.DivisionRepository
import io.reactivex.Single

class DivisionInteractorImpl(
        private val divisionRepository: DivisionRepository,
        private val schedulersProvider: SchedulersProvider
) : DivisionInteractor {

    override fun getDivisions(): Single<List<Division>> {
        return divisionRepository.getDivisions()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun saveSelectedDivision(divisionId: Int) {
        divisionRepository.saveSelectedDivision(divisionId)
    }
}
