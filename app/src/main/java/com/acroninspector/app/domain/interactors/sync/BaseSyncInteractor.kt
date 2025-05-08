package com.acroninspector.app.domain.interactors.sync

import android.util.Log
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.repositories.ExecutorRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import com.acroninspector.app.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

abstract class BaseSyncInteractor(
    private val preferencesRepository: PreferencesRepository,
    private val executorRepository: ExecutorRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val schedulersProvider: SchedulersProvider
) : SyncInteractor {


    var entityList: List<Flowable<Int>> = ArrayList()

    val functionId = preferencesRepository.functionId

    val login = preferencesRepository.login

    val divisionId = preferencesRepository.supervisedDivisionId

    protected abstract fun loadData(sessionId: String): Flowable<Int>

    protected abstract fun uploadData(sessionId: String): Flowable<Int>

    protected abstract fun uploadDataLog(sessionId: String): Completable

    protected fun saveExecutorGroupId(): Completable {
        return userRepository.getCurrentUser()
            .flatMap {
                val fullName = "${it.surname} ${it.name} ${it.thirdName}"
                executorRepository.getExecutorsByFullName(fullName)
            }.flatMapCompletable { executors ->
                if (executors.isNotEmpty()) {
                    val groupIds: List<Int> = ArrayList()
                    executors.forEach { executor ->
                        (groupIds as ArrayList).add(executor.groupNumber)
                    }

                    preferencesRepository.executorId = executors[0].id
                    preferencesRepository.executorGroupIds = groupIds
                }
                Completable.complete()
            }
    }

    override fun registerFunctionAndLoadDataFromServer(): Flowable<Int> {
        val functionId = sessionRepository.getFunctionId()

        return registerFunction(functionId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.io())
            .flatMap { sessionId ->
                Single.zip(
                    Single.just(sessionId),
                    sessionRepository.registerDevice(),
                    BiFunction<String, Boolean, String> { t1, _ -> t1 }
                )
            }
            .flatMapPublisher { sessionId ->
                loadData(sessionId)
                    .timeout(NetworkConstants.TIME_OUT_MINUTES, TimeUnit.MINUTES)
            }
            .observeOn(schedulersProvider.ui())
    }

    override fun loadDataFromServer(): Flowable<Int> {
        val sessionId = sessionRepository.getFunctionSessionId()

        return loadData(sessionId)
            .flatMapSingle { data ->
                userRepository.loadUserInfo(login, sessionId)
                    .doOnSuccess { userId -> }
                    .map { data }
                    .doOnError { e ->
                        Log.e("tests", "${e.message}")
                    }
            }
            .timeout(NetworkConstants.TIME_OUT_MINUTES, TimeUnit.MINUTES)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .doOnError { throwable ->
                Log.e("tests", "${throwable.message}")
            }
    }

    override fun uploadDataToServer(): Flowable<Int> {
        val sessionId = sessionRepository.getFunctionSessionId()

        return uploadData(sessionId)
            .flatMapSingle { data ->
                userRepository.loadUserInfo(login, sessionId)
                    .doOnSuccess { userId ->
                    }
                    .map { data
                    }
                    .doOnError { e ->
                    }
            }
            .timeout(NetworkConstants.TIME_OUT_MINUTES, TimeUnit.MINUTES)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .doOnError { throwable ->
            }
    }

    override fun uploadDataLogToServer() {
        val sessionId = sessionRepository.getFunctionSessionId()

        uploadDataLog(sessionId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(
                { Log.i("testFileLoad", "File uploaded successfully") },
                { error ->
                    Log.e("testFileLoad", "Error uploading file: ${error.message}")
                }
            )
    }

    private fun registerFunction(functionId: Int): Single<String> {
        val authSessionId = sessionRepository.getAuthSessionId()

        return sessionRepository.registerFunction(
            authSessionId, functionId,
            NetworkConstants.APP_REGISTER_FUNCTION_CODE,
            preferencesRepository.versionName
        )
    }

    override fun dataLoadedFromSeverSuccessfully() {
        preferencesRepository.isDataLoadedFromServer = true
    }

    override fun dataUploadedToSeverSuccessfully() {
        preferencesRepository.isDataUploadedToServer = true
    }

    override fun isDataLoadedFromServer(): Boolean {
        return preferencesRepository.isDataLoadedFromServer
    }

    override fun isDataUploadedToServer(): Boolean {
        return preferencesRepository.isDataUploadedToServer
    }

    // Count of entity types that will be loading
    override fun getLoadingEntityCountByFunction(): Int {
        return try {
            entityList.size / 2
        } catch (e: Exception) {
            0
        }
    }
}
