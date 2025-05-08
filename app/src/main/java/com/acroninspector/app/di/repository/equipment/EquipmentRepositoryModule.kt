package com.acroninspector.app.di.repository.equipment

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.EquipmentDao
import com.acroninspector.app.data.repositories.EquipmentRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.EquipmentRepository
import dagger.Module
import dagger.Provides

@Module
class EquipmentRepositoryModule {

    @PerScreen
    @Provides
    fun provideEquipmentRepository(dao: EquipmentDao): EquipmentRepository {
        return EquipmentRepositoryImpl(dao)
    }

    @PerScreen
    @Provides
    fun provideEquipmentDao(appDatabase: AppDatabase): EquipmentDao {
        return appDatabase.equipmentDao()
    }
}