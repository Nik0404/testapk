package com.acroninspector.app.support

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import io.reactivex.Observable

class ConnectionReceiver(private val context: Context) : BroadcastReceiver() {

    private val connectionStateSubject = io.reactivex.subjects.PublishSubject.create<Boolean>()

    var isConnected: Boolean = false
        private set

    init {
        isConnected = checkActiveConnection()
    }

    override fun onReceive(context: Context, intent: Intent) {
        isConnected = checkActiveConnection()
        connectionStateSubject.onNext(isConnected)
    }

    fun observeConnectionChanges(): Observable<Boolean> = connectionStateSubject

    @Suppress("DEPRECATION")
    private fun checkActiveConnection(): Boolean {
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}