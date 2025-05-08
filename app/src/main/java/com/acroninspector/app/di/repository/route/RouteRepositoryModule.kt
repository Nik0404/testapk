package com.acroninspector.app.di.repository.route

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.RouteDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.repositories.RouteRepositoryImpl
import com.acroninspector.app.data.util.mappers.RouteMapper
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.FunctionsApiModule
import com.acroninspector.app.domain.repositories.RouteRepository
import dagger.Module
import dagger.Provides

@Module(includes = [FunctionsApiModule::class])
class RouteRepositoryModule {

    @PerScreen
    @Provides
    fun provideRouteRepository(
            functionsApi: FunctionsApi,
            routeDao: RouteDao,
            routeMapper: RouteMapper): RouteRepository {
        return RouteRepositoryImpl(functionsApi, routeDao, routeMapper)
    }

    @PerScreen
    @Provides
    fun provideRouteDao(appDatabase: AppDatabase): RouteDao {
        return appDatabase.routeDao()
    }

    @PerScreen
    @Provides
    fun provideRouteMapper(): RouteMapper {
        return RouteMapper()
    }
}