package com.acroninspector.app.common.extension

import android.content.Context

fun Context.dpToPx(dp: Float): Float {
    return resources.displayMetrics.density * dp
}
