package com.acroninspector.app.data.repositories

import android.content.Context
import com.acroninspector.app.common.constants.PreferencesConstants
import com.acroninspector.app.domain.repositories.PreferencesRepository
import org.jetbrains.anko.defaultSharedPreferences

class PreferencesRepositoryImpl(context: Context) : PreferencesRepository {

    private val storage = context.defaultSharedPreferences

    init {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val appVersionName = packageInfo.versionName
        saveVersionName(appVersionName)
    }

    private fun saveVersionName(appVersionName: String) {
        storage.edit().putString(PreferencesConstants.VERSION_NAME, appVersionName).apply()
    }

    override var login: String
        get() = storage.getString(PreferencesConstants.LOGIN, "")!!
        set(value) {
            storage.edit().putString(PreferencesConstants.LOGIN, value).apply()
        }

    override var password: String
        get() = storage.getString(PreferencesConstants.PASSWORD, "")!!
        set(value) {
            storage.edit().putString(PreferencesConstants.PASSWORD, value).apply()
        }

    override var cardid: String
        get() = storage.getString(PreferencesConstants.CARDID, "")!!
        set(value) {
            storage.edit().putString(PreferencesConstants.CARDID, value).apply()
        }
    override var pin: String
        get() = storage.getString(PreferencesConstants.PIN, "")!!
        set(value) {
            storage.edit().putString(PreferencesConstants.PIN, value).apply()
        }

    override var functionSessionId: String
        get() = storage.getString(PreferencesConstants.SESSION_ID, "")!!
        set(value) {
            storage.edit().putString(PreferencesConstants.SESSION_ID, value).apply()
        }

    override val userId: Int
        get() = 1

    override var executorId: Int
        get() = storage.getInt(PreferencesConstants.EXECUTOR_ID, -1)
        set(value) {
            storage.edit().putInt(PreferencesConstants.EXECUTOR_ID, value).apply()
        }

    override var executorGroupIds: List<Int>
        get() {
            val executorGroupIds: ArrayList<Int> = ArrayList()
            val groupIds = storage.getString(PreferencesConstants.EXECUTOR_GROUP_ID, "")?.split(",")

            groupIds?.forEach { groupId ->
                try {
                    executorGroupIds.add(groupId.trim().toInt())
                } catch (e: NumberFormatException) {
                    //Do nothing
                }
            }

            return executorGroupIds
        }
        set(value) {
            val groupIds = value.joinToString()
            storage.edit().putString(PreferencesConstants.EXECUTOR_GROUP_ID, groupIds).apply()
        }

    override var functionId: Int
        get() = storage.getInt(PreferencesConstants.FUNCTION_ID, -1)
        set(value) {
            storage.edit().putInt(PreferencesConstants.FUNCTION_ID, value).apply()
        }

    override var authSessionId: String
        get() = storage.getString(PreferencesConstants.AUTH_SESSION_ID, "")!!
        set(value) {
            storage.edit().putString(PreferencesConstants.AUTH_SESSION_ID, value).apply()
        }

    override var versionName: String
        get() = storage.getString(PreferencesConstants.VERSION_NAME, "")!!
        set(value) { saveVersionName(value) }

    override var supervisedDivisionId: Int
        get() = storage.getInt(PreferencesConstants.SUPERVISED_DIVISION_ID, -1)
        set(value) {
            storage.edit().putInt(PreferencesConstants.SUPERVISED_DIVISION_ID, value).apply()
        }

    override var isDataLoadedFromServer: Boolean
        get() = storage.getBoolean(PreferencesConstants.IS_DATA_LOADED_FROM_SERVER, false)
        set(value) {
            storage.edit().putBoolean(PreferencesConstants.IS_DATA_LOADED_FROM_SERVER, value).apply()
        }

    override var isDataUploadedToServer: Boolean
        get() = storage.getBoolean(PreferencesConstants.IS_DATA_UPLOADED_TO_SERVER, true)
        set(value) {
            storage.edit().putBoolean(PreferencesConstants.IS_DATA_UPLOADED_TO_SERVER, value).apply()
        }

    override var deviceId: String
        get() = storage.getString(PreferencesConstants.DEVICE_ID, "")!!
        set(value) {
            storage.edit().putString(PreferencesConstants.DEVICE_ID, value).apply()
        }

    override fun clear() {
        storage.edit().clear().apply()
    }
}
