package com.acroninspector.app.di.repository.equipmentclass

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.EquipmentClassDao
import com.acroninspector.app.data.repositories.EquipmentClassRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.EquipmentClassRepository
import dagger.Module
import dagger.Provides

@Module
class EquipmentClassRepositoryModule {

    @PerScreen
    @Provides
    fun provideEquipmentClassRepository(equipmentClassDao: EquipmentClassDao): EquipmentClassRepository {
        return EquipmentClassRepositoryImpl(equipmentClassDao)
    }

    @PerScreen
    @Provides
    fun provideEquipmentClassDao(appDatabase: AppDatabase): EquipmentClassDao {
        return appDatabase.equipmentClassDao()
    }
}