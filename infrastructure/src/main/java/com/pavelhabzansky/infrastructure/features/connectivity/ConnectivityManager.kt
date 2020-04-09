package com.pavelhabzansky.infrastructure.features.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.pavelhabzansky.domain.features.connectivity.manager.IConnectivityManager

class ConnectivityManager(private val appContext: Context) : IConnectivityManager {

    override fun createNetworkReceiver(callback: (Boolean) -> Unit): BroadcastReceiver {
        return (object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                callback(isConnected())
            }
        })
    }

    override fun isConnected(): Boolean {
        val info = getNetworkInfo(appContext)
        return info != null && info.isConnected
    }

    private fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm?.activeNetworkInfo
    }


}