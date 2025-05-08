package com.acroninspector.app.di.repository.api

import com.acroninspector.app.data.datasource.network.UserApi
import com.acroninspector.app.di.global.scope.PerScreen
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UserApiModule {

    @PerScreen
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}