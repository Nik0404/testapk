package com.acroninspector.app.domain.interactors.division

import com.acroninspector.app.domain.entity.local.database.Division
import io.reactivex.Single

interface DivisionInteractor {

    fun getDivisions(): Single<List<Division>>

    fun saveSelectedDivision(divisionId: Int)
}
