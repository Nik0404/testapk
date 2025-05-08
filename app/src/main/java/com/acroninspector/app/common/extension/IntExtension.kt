package com.acroninspector.app.common.extension

import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import timber.log.Timber
import java.lang.Exception

fun Int.toAcronId(): String = try {
    if (this == DEFAULT_INVALID_ID) {
        ""
    } else this.toString()
} catch (e: Exception) {
    Timber.e(e)
    this.toString()
}