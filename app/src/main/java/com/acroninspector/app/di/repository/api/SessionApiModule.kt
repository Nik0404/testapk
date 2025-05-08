package com.acroninspector.app.di.repository.api

import com.acroninspector.app.data.datasource.network.SessionApi
import com.acroninspector.app.di.global.scope.PerScreen
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SessionApiModule {

    @PerScreen
    @Provides
    fun provideSessionApi(retrofit: Retrofit): SessionApi {
        return retrofit.create(SessionApi::class.java)
    }
}