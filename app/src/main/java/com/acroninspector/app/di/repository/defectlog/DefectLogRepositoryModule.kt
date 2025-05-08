package com.acroninspector.app.di.repository.defectlog

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.DefectLogDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.repositories.DefectLogRepositoryImpl
import com.acroninspector.app.data.util.mappers.DefectLogMapper
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.FunctionsApiModule
import com.acroninspector.app.domain.repositories.DefectLogRepository
import dagger.Module
import dagger.Provides

@Module(includes = [FunctionsApiModule::class])
class DefectLogRepositoryModule {

    @PerScreen
    @Provides
    fun provideDefectLogRepository(
            functionsApi: FunctionsApi,
            defectLogDao: DefectLogDao,
            defectLogMapper: DefectLogMapper
    ): DefectLogRepository {
        return DefectLogRepositoryImpl(functionsApi, defectLogDao, defectLogMapper)
    }

    @PerScreen
    @Provides
    fun provideDefectLogDao(appDatabase: AppDatabase): DefectLogDao {
        return appDatabase.defectLogDao()
    }

    @PerScreen
    @Provides
    fun provideDefectLogMapper(): DefectLogMapper {
        return DefectLogMapper()
    }
}