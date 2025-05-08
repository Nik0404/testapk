package com.acroninspector.app.di.repository.localdefect

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.LocalDefectDao
import com.acroninspector.app.data.repositories.LocalDefectRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import dagger.Module
import dagger.Provides

@Module
class LocalDefectRepositoryModule {

    @PerScreen
    @Provides
    fun provideLocalDefectRepository(localDefectDao: LocalDefectDao): LocalDefectRepository {
        return LocalDefectRepositoryImpl(localDefectDao)
    }

    @PerScreen
    @Provides
    fun provideLocalDefectDao(appDatabase: AppDatabase): LocalDefectDao {
        return appDatabase.localDefectDao()
    }
}