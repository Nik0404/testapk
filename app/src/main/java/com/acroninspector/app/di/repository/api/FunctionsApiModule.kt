package com.acroninspector.app.di.repository.api

import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.di.global.scope.PerScreen
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class FunctionsApiModule {

    @PerScreen
    @Provides
    fun provideFunctionsApi(retrofit: Retrofit): FunctionsApi {
        return retrofit.create(FunctionsApi::class.java)
    }
}