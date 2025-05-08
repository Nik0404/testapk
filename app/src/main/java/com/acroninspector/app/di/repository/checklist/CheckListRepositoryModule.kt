package com.acroninspector.app.di.repository.checklist

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.CheckListDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.repositories.CheckListRepositoryImpl
import com.acroninspector.app.data.util.mappers.CheckListMapper
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.FunctionsApiModule
import com.acroninspector.app.domain.repositories.CheckListRepository
import dagger.Module
import dagger.Provides

@Module(includes = [FunctionsApiModule::class])
class CheckListRepositoryModule {

    @PerScreen
    @Provides
    fun provideCheckListRepository(
            functionsApi: FunctionsApi,
            checkListDao: CheckListDao,
            checkListMapper: CheckListMapper): CheckListRepository {
        return CheckListRepositoryImpl(functionsApi, checkListDao, checkListMapper)
    }

    @PerScreen
    @Provides
    fun provideCheckListDao(appDatabase: AppDatabase): CheckListDao {
        return appDatabase.checkListDao()
    }

    @PerScreen
    @Provides
    fun provideCheckListMapper(): CheckListMapper {
        return CheckListMapper()
    }
}