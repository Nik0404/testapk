package com.acroninspector.app.data.repositories

import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.common.utils.DateUtil
import com.acroninspector.app.data.datasource.database.dao.FunctionDao
import com.acroninspector.app.data.datasource.network.SessionApi
import com.acroninspector.app.data.util.mappers.base.EntityMapper
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.entity.remote.request.DetailsRequest
import com.acroninspector.app.domain.entity.remote.request.LoginHostRequest
import com.acroninspector.app.domain.entity.remote.request.LoginPinHostRequest
import com.acroninspector.app.domain.entity.remote.request.LogoutRequest
import com.acroninspector.app.domain.entity.remote.request.ModifyingDataRequest
import com.acroninspector.app.domain.entity.remote.request.RegisterFunctionRequest
import com.acroninspector.app.domain.entity.remote.request.RegisterReleasesRequest
import com.acroninspector.app.domain.entity.remote.request.values.DeviceRegistrationValue
import com.acroninspector.app.domain.entity.remote.response.ReleasesResponse
import com.acroninspector.app.domain.entity.remote.schema.FunctionSchema
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.util.Calendar

class SessionRepositoryImpl(
    private val sessionApi: SessionApi,
    private val userFunctionDao: FunctionDao,
    private val userFunctionMapper: EntityMapper<FunctionSchema, UserFunction>,
    private val preferencesRepository: PreferencesRepository
) : SessionRepository {

    override fun loginUser(
        login: String,
        password: String,
        appCode: String,
        appVersion: String
    ): Single<String> {
        val loginRequest = LoginHostRequest(login, password, appCode, appVersion)
        return sessionApi.loginHost(loginRequest)
            .doOnSuccess {
                preferencesRepository.authSessionId = it.sessionId
                preferencesRepository.login = login
                preferencesRepository.password = password
            }.flatMap { response ->
                val functions = response.functions.map { function ->
                    userFunctionMapper.fromSchemaToEntity(function)
                }
                userFunctionDao.saveFunctions(functions)
            }.flatMap {
                Single.just(preferencesRepository.authSessionId)
            }
    }

    override fun getReleases(sessionId: String, appCode: String): Single<List<ReleasesResponse>> {
        val releasesBody = RegisterReleasesRequest(sessionId, appCode)

        return sessionApi.registerReleases(releasesBody)
    }

    override fun getDetails(sessionId: String, releaseid: String, appCode: String): Single<String> {
        val releasesBody = DetailsRequest(sessionId, releaseid, appCode)

        return sessionApi.getDetails(releasesBody)
    }

    override fun loginPinUser(
        cardid: String,
        pin: String,
        appCode: String,
        appVersion: String
    ): Single<String> {
        val loginRequest = LoginPinHostRequest(cardid, pin, appCode, appVersion)
        return sessionApi.loginPinHost(loginRequest)
            .doOnSuccess {
                preferencesRepository.authSessionId = it.sessionId
                preferencesRepository.cardid = cardid
                preferencesRepository.pin = pin
            }.flatMap { response ->
                val functions = response.functions.map { function ->
                    userFunctionMapper.fromSchemaToEntity(function)
                }
                userFunctionDao.saveFunctions(functions)
            }.flatMap {
                Single.just(preferencesRepository.authSessionId)
            }

    }

    override fun registerFunction(
        sessionId: String,
        functionId: Int,
        appCode: String,
        appVersion: String
    ): Single<String> {
        val requestBody = RegisterFunctionRequest(sessionId, appCode, appVersion, functionId)

        return sessionApi.registerFunction(requestBody)
            .doOnSuccess { preferencesRepository.functionSessionId = it.sessionId }
            .map { it.sessionId }
    }

    override fun logout(logoutRequest: LogoutRequest): Completable {
        return sessionApi.logout(logoutRequest)
    }

    override fun getFunctions(): Single<List<UserFunction>> {
        return userFunctionDao.getFunctions()
    }

    override fun saveSelectedFunctionId(functionId: Int) {
        preferencesRepository.functionId = functionId
    }

    override fun getAuthSessionId(): String = preferencesRepository.authSessionId

    override fun getFunctionSessionId(): String = preferencesRepository.functionSessionId

    override fun getFunctionId(): Int = preferencesRepository.functionId

    override fun getLogin(): String {
        return preferencesRepository.login
    }


    override fun getPassword(): String = preferencesRepository.password

    override fun getAppVersionName(): String = preferencesRepository.versionName

    override fun registerDevice(): Single<Boolean> {
        val registrationRecord = DeviceRegistrationValue(
            preferencesRepository.deviceId,
            DateUtil.convertLongDateToString(Calendar.getInstance().timeInMillis)
        )
        val requestBody = ModifyingDataRequest(
            getFunctionSessionId(), getFunctionId(),
            FunctionNames.REGISTER_DEVICE, registrationRecord
        )
        return sessionApi.registerDevice(requestBody).toSingle { true }
    }
}
