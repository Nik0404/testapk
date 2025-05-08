package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.DivisionDao
import com.acroninspector.app.data.datasource.network.SessionApi
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.entity.remote.request.GettingDivisionsRequest
import com.acroninspector.app.domain.entity.remote.schema.DivisionSchema
import com.acroninspector.app.domain.repositories.DivisionRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import io.reactivex.Completable
import io.reactivex.Single

class DivisionRepositoryImpl(
        private val sessionApi: SessionApi,
        private val divisionDao: DivisionDao,
        private val divisionMapper: EntityMapper<DivisionSchema, Division>,
        private val preferencesRepository: PreferencesRepository
) : DivisionRepository {

    override fun loadDivisionsFromServer(sessionId: String, functionId: Int): Completable {
        val request = GettingDivisionsRequest(sessionId, functionId)

        return divisionDao.deleteDivisions()
                .andThen(sessionApi.getDivisions(request))
                .map { response ->
                    response.divisions.map { divisionMapper.fromSchemaToEntity(it) }
                }.flatMapCompletable { divisionDao.saveDivisions(it) }
    }

    override fun getDivisions(): Single<List<Division>> {
        return divisionDao.getDivisions()
    }

    override fun getDivisionNameById(divisionId: Int): Single<String> {
        return divisionDao.getDivisionNameById(divisionId)
    }

    override fun saveSelectedDivision(divisionId: Int) {
        preferencesRepository.supervisedDivisionId = divisionId
    }

    override fun getSelectedDivisionId(): Int {
        return preferencesRepository.supervisedDivisionId
    }
}
