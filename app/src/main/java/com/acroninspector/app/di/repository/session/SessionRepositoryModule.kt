package com.acroninspector.app.di.repository.session

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.FunctionDao
import com.acroninspector.app.data.datasource.network.SessionApi
import com.acroninspector.app.data.repositories.SessionRepositoryImpl
import com.acroninspector.app.data.util.mappers.FunctionMapper
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.SessionApiModule
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.entity.remote.schema.FunctionSchema
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import dagger.Module
import dagger.Provides

@Module(includes = [SessionApiModule::class])
class SessionRepositoryModule {

    @PerScreen
    @Provides
    fun provideSessionRepository(
            sessionApi: SessionApi,
            userFunctionDao: FunctionDao,
            userFunctionMapper: EntityMapper<FunctionSchema, UserFunction>,
            preferencesRepository: PreferencesRepository
    ): SessionRepository {
        return SessionRepositoryImpl(sessionApi, userFunctionDao, userFunctionMapper, preferencesRepository)
    }

    @PerScreen
    @Provides
    fun provideFunctionDao(appDatabase: AppDatabase): FunctionDao {
        return appDatabase.userFunctionDao()
    }

    @PerScreen
    @Provides
    fun provideFunctionMapper(): EntityMapper<FunctionSchema, UserFunction> {
        return FunctionMapper()
    }
}