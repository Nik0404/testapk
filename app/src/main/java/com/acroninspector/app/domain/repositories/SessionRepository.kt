package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.entity.remote.request.LogoutRequest
import com.acroninspector.app.domain.entity.remote.response.ReleasesResponse
import io.reactivex.Completable
import io.reactivex.Single

interface SessionRepository {

    fun loginUser(login: String, password: String, appCode: String, appVersion: String): Single<String>

    fun loginPinUser(cardid: String, pin: String, appCode: String, appVersion: String): Single<String>

    fun logout(logoutRequest: LogoutRequest): Completable

    fun getFunctions(): Single<List<UserFunction>>

    fun saveSelectedFunctionId(functionId: Int)

    fun registerFunction(sessionId: String, functionId: Int, appCode: String, appVersion: String): Single<String>

    fun getAuthSessionId(): String

    fun getFunctionSessionId(): String

    fun getFunctionId(): Int

    fun getLogin(): String

    fun getPassword(): String

    fun getAppVersionName(): String

    fun registerDevice(): Single<Boolean>

    fun getReleases(sessionId: String, appCode: String): Single<List<ReleasesResponse>>

    fun getDetails(sessionId: String, releaseid: String, appCode: String): Single<String>

}
