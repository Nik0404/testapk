package com.acroninspector.app.di.repository.nfc

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.NfcEquipmentDao
import com.acroninspector.app.data.datasource.database.dao.NfcRouteDao
import com.acroninspector.app.data.repositories.NfcRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.NfcRepository
import dagger.Module
import dagger.Provides

@Module
class NfcRepositoryModule {

    @PerScreen
    @Provides
    fun provideNfcRepository(
            nfcRouteDao: NfcRouteDao,
            nfcEquipmentDao: NfcEquipmentDao
    ): NfcRepository {
        return NfcRepositoryImpl(nfcRouteDao, nfcEquipmentDao)
    }

    @PerScreen
    @Provides
    fun provideNfcRouteDao(appDatabase: AppDatabase): NfcRouteDao {
        return appDatabase.nfcRouteDao()
    }

    @PerScreen
    @Provides
    fun provideNfcEquipmentDao(appDatabase: AppDatabase): NfcEquipmentDao {
        return appDatabase.nfcEquipmentDao()
    }
}