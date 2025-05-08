package com.acroninspector.app.domain.interactors.login

import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.entity.remote.response.ReleasesResponse
import io.reactivex.Single

interface LoginInteractor {

    fun loginUser(login: String, password: String, isPin: Boolean): Single<List<UserFunction>>

    fun getFunctions(): Single<List<UserFunction>>

    fun saveSelectedFunctionId(functionId: Int)

    fun getDivisionsFromServer(): Single<List<Division>>

    fun saveSelectedDivisionId(divisionId: Int)

    fun getSelectedDivisionId(): Int

    fun getDivisions(): Single<List<Division>>

    fun getLogin(): String

    fun getPassword(): String

    fun getSessionId(): String

    fun getAppVersionName(): String

    fun getDeviceId(): String

    fun setDeviceId(deviceId: String)

    fun getIsaReleases(): Single<List<ReleasesResponse>>

    fun getReleaseDetails(releaseid: String): Single<String>
}