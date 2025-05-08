package com.acroninspector.app.domain.interactors.main

import com.acroninspector.app.domain.entity.local.database.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface MainInteractor {

    fun refreshSessionId(): Completable

    fun getCurrentUser(): Single<User>

    fun getNotificatoinsCount(): Flowable<Int>

    fun logout(): Completable

    fun getCurrentFunctionId(): Int

    fun getAppVersionName(): String
}