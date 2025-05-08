package com.acroninspector.app.di.repository.division

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.DivisionDao
import com.acroninspector.app.data.datasource.network.SessionApi
import com.acroninspector.app.data.repositories.DivisionRepositoryImpl
import com.acroninspector.app.data.util.mappers.DivisionMapper
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.SessionApiModule
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.entity.remote.schema.DivisionSchema
import com.acroninspector.app.domain.repositories.DivisionRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import dagger.Module
import dagger.Provides

@Module(includes = [SessionApiModule::class])
class DivisionRepositoryModule {

    @PerScreen
    @Provides
    fun provideDivisionRepository(
            sessionApi: SessionApi,
            divisionDao: DivisionDao,
            divisionMapper: EntityMapper<DivisionSchema, Division>,
            preferencesRepository: PreferencesRepository
    ): DivisionRepository {
        return DivisionRepositoryImpl(sessionApi, divisionDao, divisionMapper, preferencesRepository)
    }

    @PerScreen
    @Provides
    fun provideDivisionDao(appDatabase: AppDatabase): DivisionDao {
        return appDatabase.divisionDao()
    }

    @PerScreen
    @Provides
    fun provideDivisionMapper(): EntityMapper<DivisionSchema, Division> {
        return DivisionMapper()
    }
}