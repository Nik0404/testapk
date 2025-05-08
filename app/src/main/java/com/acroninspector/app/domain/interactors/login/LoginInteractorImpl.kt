package com.acroninspector.app.domain.interactors.login

import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.Division
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.entity.remote.response.ReleasesResponse
import com.acroninspector.app.domain.repositories.DivisionRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import com.acroninspector.app.domain.repositories.UserRepository
import io.reactivex.Single

class LoginInteractorImpl(
    private val sessionRepository: SessionRepository,
    private val divisionRepository: DivisionRepository,
    private val userRepository: UserRepository,
    private val schedulersProvider: SchedulersProvider,
    private val preferencesRepository: PreferencesRepository
) : LoginInteractor {

    override fun loginUser(
        login: String,
        password: String,
        isPin: Boolean
    ): Single<List<UserFunction>> {
        return if (isPin) {
            sessionRepository.loginPinUser(
                login, password,
                NetworkConstants.APP_LOGIN_HOST_CODE,
                getAppVersionName()
            )
        } else {
            sessionRepository.loginUser(
                login, password,
                NetworkConstants.APP_LOGIN_HOST_CODE,
                getAppVersionName()
            )
        }
            .subscribeOn(schedulersProvider.io())
            .flatMap { userRepository.loadUserInfo(login, it) }
            .flatMap { sessionRepository.getFunctions() }
            .observeOn(schedulersProvider.ui())
    }

    override fun getIsaReleases(): Single<List<ReleasesResponse>> {
        val authSessionId = sessionRepository.getAuthSessionId()

        return sessionRepository.getReleases(
            authSessionId,
            NetworkConstants.APP_REGISTER_FUNCTION_CODE
        )
            .subscribeOn(schedulersProvider.io())
    }

    override fun getReleaseDetails(releaseid: String): Single<String> {
        val authSessionId = sessionRepository.getAuthSessionId()

        return sessionRepository.getDetails(
            authSessionId,
            releaseid,
            NetworkConstants.APP_REGISTER_FUNCTION_CODE
        )
            .subscribeOn(schedulersProvider.io())
    }

    override fun getFunctions(): Single<List<UserFunction>> {
        return sessionRepository.getFunctions()
            .map { it.filter { uf -> uf.functionCode in Functions.supportedFunctions } }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    override fun saveSelectedFunctionId(functionId: Int) {
        sessionRepository.saveSelectedFunctionId(functionId)
    }

    override fun getDivisionsFromServer(): Single<List<Division>> {
        val functionId = sessionRepository.getFunctionId()
        val authSessionId = sessionRepository.getAuthSessionId()

        return sessionRepository.registerFunction(
            authSessionId, functionId,
            NetworkConstants.APP_REGISTER_FUNCTION_CODE,
            getAppVersionName()
        ).subscribeOn(schedulersProvider.io())
            .flatMapCompletable { sessionId ->
                divisionRepository.loadDivisionsFromServer(sessionId, functionId)
            }.andThen(divisionRepository.getDivisions())
            .observeOn(schedulersProvider.ui())
    }

    override fun saveSelectedDivisionId(divisionId: Int) {
        divisionRepository.saveSelectedDivision(divisionId)
    }

    override fun getDivisions(): Single<List<Division>> {
        return divisionRepository.getDivisions()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    override fun getSessionId(): String = sessionRepository.getFunctionSessionId()

    override fun getLogin(): String = sessionRepository.getLogin()

    override fun getPassword(): String = sessionRepository.getPassword()

    override fun getSelectedDivisionId(): Int = divisionRepository.getSelectedDivisionId()

    override fun getAppVersionName(): String = sessionRepository.getAppVersionName()

    override fun getDeviceId(): String = preferencesRepository.deviceId

    override fun setDeviceId(deviceId: String) {
        preferencesRepository.deviceId = deviceId;
    }
}
