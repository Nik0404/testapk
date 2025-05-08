package com.acroninspector.app.di.repository.defectcause

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.DefectCauseDao
import com.acroninspector.app.data.repositories.DefectCauseRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.DefectCauseRepository
import dagger.Module
import dagger.Provides

@Module
class DefectCauseRepositoryModule {

    @PerScreen
    @Provides
    fun provideDefectCauseRepository(defectCauseDao: DefectCauseDao): DefectCauseRepository {
        return DefectCauseRepositoryImpl(defectCauseDao)
    }

    @PerScreen
    @Provides
    fun provideDefectCauseDao(appDatabase: AppDatabase): DefectCauseDao {
        return appDatabase.defectCauseDao()
    }
}