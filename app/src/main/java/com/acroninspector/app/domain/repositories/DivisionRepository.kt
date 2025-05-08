package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.Division
import io.reactivex.Completable
import io.reactivex.Single

interface DivisionRepository {

    fun loadDivisionsFromServer(sessionId: String, functionId: Int): Completable

    fun getDivisions(): Single<List<Division>>

    fun getDivisionNameById(divisionId: Int): Single<String>

    fun saveSelectedDivision(divisionId: Int)

    fun getSelectedDivisionId(): Int
}
