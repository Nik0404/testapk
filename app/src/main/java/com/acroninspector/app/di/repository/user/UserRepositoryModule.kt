package com.acroninspector.app.di.repository.user

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.UserDao
import com.acroninspector.app.data.datasource.database.dao.UserGroupDao
import com.acroninspector.app.data.datasource.network.UserApi
import com.acroninspector.app.data.repositories.UserRepositoryImpl
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.UserApiModule
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides

@Module(includes = [UserApiModule::class])
class UserRepositoryModule {

    @PerScreen
    @Provides
    fun provideUserRepository(
            userApi: UserApi,
            userDao: UserDao,
            userGroupDao: UserGroupDao,
            preferencesRepository: PreferencesRepository
    ): UserRepository {
        return UserRepositoryImpl(userApi, userDao, userGroupDao, preferencesRepository)
    }

    @PerScreen
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @PerScreen
    @Provides
    fun provideUserGroupDao(appDatabase: AppDatabase): UserGroupDao {
        return appDatabase.userGroupDao()
    }
}