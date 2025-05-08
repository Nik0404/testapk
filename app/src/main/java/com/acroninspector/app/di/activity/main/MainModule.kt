package com.acroninspector.app.di.activity.main

import android.content.Context
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.NfcF
import android.nfc.tech.NfcV
import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.datasource.network.SessionApi
import com.acroninspector.app.data.datasource.network.UserApi
import com.acroninspector.app.data.repositories.SyncRepositoryImpl
import com.acroninspector.app.data.util.mappers.base.MapperFactory
import com.acroninspector.app.di.global.base.BaseModule
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.FunctionsApiModule
import com.acroninspector.app.di.repository.api.SessionApiModule
import com.acroninspector.app.di.repository.executor.ExecutorRepositoryModule
import com.acroninspector.app.di.repository.notifications.NotificationsRepositoryModule
import com.acroninspector.app.di.repository.session.SessionRepositoryModule
import com.acroninspector.app.di.repository.user.UserRepositoryModule
import com.acroninspector.app.domain.interactors.main.MainInteractor
import com.acroninspector.app.domain.interactors.main.MainInteractorImpl
import com.acroninspector.app.domain.interactors.sync.SyncInteractor
import com.acroninspector.app.domain.interactors.sync.SyncInteractorByPassImpl
import com.acroninspector.app.domain.interactors.sync.SyncInteractorByPassManagementImpl
import com.acroninspector.app.domain.interactors.sync.SyncInteractorRegisteringTagImpl
import com.acroninspector.app.domain.interactors.usercard.UserCardInteractor
import com.acroninspector.app.domain.interactors.usercard.UserCardInteractorImpl
import com.acroninspector.app.domain.repositories.ExecutorRepository
import com.acroninspector.app.domain.repositories.NotificationsRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import com.acroninspector.app.domain.repositories.SyncRepository
import com.acroninspector.app.domain.repositories.UserRepository
import com.acroninspector.app.presentation.activity.main.MainPresenter
import com.acroninspector.app.presentation.dialog.BrightnessDialog
import com.acroninspector.app.presentation.dialog.UserCardDialog
import com.acroninspector.app.presentation.dialog.UserLoginDialog
import com.acroninspector.app.support.ConnectionProvider
import dagger.Module
import dagger.Provides


@Module(includes = [
    UserRepositoryModule::class,
    NotificationsRepositoryModule::class,
    SessionRepositoryModule::class,
    ExecutorRepositoryModule::class,
    FunctionsApiModule::class,
    SessionApiModule::class])
class MainModule(private val applicationContext: Context) : BaseModule {

    @PerScreen
    @Provides
    fun providePresenter(
            mainInteractor: MainInteractor,
            syncInteractor: SyncInteractor,
            networkErrorsParser: NetworkErrorsParser
    ): MainPresenter {
        return MainPresenter(mainInteractor, syncInteractor, networkErrorsParser)
    }

    @PerScreen
    @Provides
    fun provideNetworkErrorsParser(): NetworkErrorsParser {
        return NetworkErrorsParser()
    }

    @PerScreen
    @Provides
    fun provideMainInteractor(
            sessionRepository: SessionRepository,
            userRepository: UserRepository,
            notificationsRepository: NotificationsRepository,
            preferencesRepository: PreferencesRepository,
            appDatabase: AppDatabase,
            schedulersProvider: SchedulersProvider
    ): MainInteractor {
        return MainInteractorImpl(sessionRepository, userRepository, notificationsRepository,
                preferencesRepository, appDatabase, schedulersProvider)
    }

    @PerScreen
    @Provides
    fun provideSyncInteractor(
            syncRepository: SyncRepository,
            sessionRepository: SessionRepository,
            executorRepository: ExecutorRepository,
            userRepository: UserRepository,
            preferencesRepository: PreferencesRepository,
            schedulersProvider: SchedulersProvider
    ): SyncInteractor {
        return when (val functionId = preferencesRepository.functionId) {
            Functions.BYPASS -> SyncInteractorByPassImpl(
                    syncRepository,
                    preferencesRepository,
                    executorRepository,
                    sessionRepository,
                    userRepository,
                    schedulersProvider
            )
            Functions.BYPASS_MANAGEMENT -> SyncInteractorByPassManagementImpl(
                    syncRepository,
                    preferencesRepository,
                    executorRepository,
                    sessionRepository,
                    userRepository,
                    schedulersProvider
            )
            Functions.REGISTERING_LABELS -> SyncInteractorRegisteringTagImpl(
                    syncRepository,
                    preferencesRepository,
                    executorRepository,
                    sessionRepository,
                    userRepository,
                    schedulersProvider
            )
            else -> throw IllegalArgumentException("Unknown function: $functionId")
        }
    }

    @PerScreen
    @Provides
    fun provideSyncRepository(
            appDatabase: AppDatabase,
            userApi: UserApi,
            sessionApi: SessionApi,
            functionsApi: FunctionsApi,
            mapperFactory: MapperFactory,
            preferencesRepository: PreferencesRepository
    ): SyncRepository {
        return SyncRepositoryImpl(
                appDatabase,
                userApi,
                sessionApi,
                functionsApi,
                mapperFactory,
                preferencesRepository)
    }

    @PerScreen
    @Provides
    fun provideMapperFactory(): MapperFactory {
        return MapperFactory()
    }

    @PerScreen
    @Provides
    fun provideUserCardInteractor(userRepository: UserRepository,
                                  preferencesRepository: PreferencesRepository,
                                  schedulersProvider: SchedulersProvider): UserCardInteractor {
        return UserCardInteractorImpl(userRepository, preferencesRepository, schedulersProvider)
    }

    @PerScreen
    @Provides
    fun provideConnectionProvider(): ConnectionProvider {
        return ConnectionProvider(applicationContext)
    }

    @Provides
    fun provideUserCardDialog(userCardInteractor: UserCardInteractor): UserCardDialog {
        return UserCardDialog(userCardInteractor)
    }

    @Provides
    fun provideUserLoginDialog(sessionRepository: SessionRepository): UserLoginDialog {
        return UserLoginDialog(sessionRepository)
    }

    @Provides
    fun provideBrightnessDialog(): BrightnessDialog {
        return BrightnessDialog()
    }

    @PerScreen
    @Provides
    fun provideTechListArray(): Array<Array<String>> {
        return arrayOf(arrayOf(
                NfcA::class.java.name,
                NfcB::class.java.name,
                NfcF::class.java.name,
                NfcV::class.java.name,
                IsoDep::class.java.name,
                MifareClassic::class.java.name,
                MifareUltralight::class.java.name,
                Ndef::class.java.name
        ))
    }
}
