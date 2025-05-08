package com.acroninspector.app.di.repository.defect

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.DefectDao
import com.acroninspector.app.data.repositories.DefectRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.DefectRepository
import dagger.Module
import dagger.Provides

@Module
class DefectRepositoryModule {

    @PerScreen
    @Provides
    fun provideDefectRepository(defectDao: DefectDao): DefectRepository {
        return DefectRepositoryImpl(defectDao)
    }

    @PerScreen
    @Provides
    fun provideDefectDao(appDatabase: AppDatabase): DefectDao {
        return appDatabase.defectDao()
    }
}