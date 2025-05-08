package com.acroninspector.app.di.repository.search

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.SearchHistoryDao
import com.acroninspector.app.data.repositories.SearchRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.domain.repositories.SearchRepository
import dagger.Module
import dagger.Provides

@Module
class SearchRepositoryModule {

    @PerScreen
    @Provides
    fun provideSearchRepository(searchHistoryDao: SearchHistoryDao): SearchRepository {
        return SearchRepositoryImpl(searchHistoryDao)
    }

    @PerScreen
    @Provides
    fun provideSearchHistoryDao(appDatabase: AppDatabase): SearchHistoryDao {
        return appDatabase.searchHistoryDao()
    }
}