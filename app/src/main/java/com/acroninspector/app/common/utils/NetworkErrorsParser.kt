package com.acroninspector.app.common.utils

import org.json.JSONObject
import java.lang.Exception

class NetworkErrorsParser {

    fun parseErrorMessage(errorBody: String): String {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("errorUserMessage")
        } catch (e: Exception) {
            ""
        }
    }

    fun parseErrorCode(errorBody: String): Int {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getInt("errorCode")
        } catch (e: Exception) {
            0
        }
    }

    fun parseErrorUrl(errorBody: String): String {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("url")
        } catch (e: Exception) {
            ""
        }
    }

}