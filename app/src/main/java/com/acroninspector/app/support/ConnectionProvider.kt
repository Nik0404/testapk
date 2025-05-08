package com.acroninspector.app.support

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import io.reactivex.Observable

class ConnectionProvider(context: Context) {

    private val receiver: ConnectionReceiver = ConnectionReceiver(context)

    init {
        val intentFilter = IntentFilter()

        @Suppress("DEPRECATION")
        intentFilter.addAction(CONNECTIVITY_ACTION)

        context.registerReceiver(receiver, intentFilter)
    }

    fun observeConnectionChanges(): Observable<Boolean> = receiver.observeConnectionChanges()

    fun isConnected() = receiver.isConnected

    fun isDisconnected() = !receiver.isConnected

    companion object {
        const val LAST_CONNECTION_QUALITY_RESULT_CACHE_TIME = 60L * 1000
    }
}