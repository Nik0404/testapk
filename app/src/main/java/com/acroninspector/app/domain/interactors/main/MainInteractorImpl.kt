package com.acroninspector.app.domain.interactors.main

import android.util.Log
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.domain.entity.local.database.User
import com.acroninspector.app.domain.entity.remote.request.LogoutRequest
import com.acroninspector.app.domain.repositories.NotificationsRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import com.acroninspector.app.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class MainInteractorImpl(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val notificationsRepository: NotificationsRepository,
    private val preferencesRepository: PreferencesRepository,
    private val appDatabase: AppDatabase,
    private val schedulersProvider: SchedulersProvider
) : MainInteractor {

    override fun getCurrentUser(): Single<User> {
        return userRepository.getCurrentUser()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    override fun getNotificatoinsCount(): Flowable<Int> {
        return notificationsRepository.getUnreadedNotifications()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.computation())
            .map { it.size }
            .observeOn(schedulersProvider.ui())
    }

    override fun logout(): Completable {
        val currentSessionId = preferencesRepository.functionSessionId
        val logoutRequest = LogoutRequest(currentSessionId, NetworkConstants.APP_LOGIN_HOST_CODE)

        return sessionRepository.logout(logoutRequest)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.io())
            .doOnEvent {
                val login = preferencesRepository.login
                val password = preferencesRepository.password
                val appVersion = preferencesRepository.versionName

                preferencesRepository.clear()

                preferencesRepository.login = login
                preferencesRepository.password = password
                preferencesRepository.versionName = appVersion

                appDatabase.clearAllTables()
            }.observeOn(schedulersProvider.ui())
    }

    override fun getCurrentFunctionId(): Int {
        return sessionRepository.getFunctionId()
    }

    override fun refreshSessionId(): Completable {
        val cardId = preferencesRepository.cardid
        return if (cardId.isEmpty()) {
            sessionRepository.loginUser(
                preferencesRepository.login,
                preferencesRepository.password,
                NetworkConstants.APP_LOGIN_HOST_CODE,
                getAppVersionName()
            )
        } else {
            sessionRepository.loginPinUser(
                preferencesRepository.cardid,
                preferencesRepository.pin,
                NetworkConstants.APP_LOGIN_HOST_CODE,
                getAppVersionName()
            )
        }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.io())
            .flatMap {
                sessionRepository.registerFunction(
                    it, preferencesRepository.functionId,
                    NetworkConstants.APP_REGISTER_FUNCTION_CODE,
                    getAppVersionName()
                )
            }.ignoreElement()
            .observeOn(schedulersProvider.ui())
    }


    override fun getAppVersionName(): String = preferencesRepository.versionName
}
