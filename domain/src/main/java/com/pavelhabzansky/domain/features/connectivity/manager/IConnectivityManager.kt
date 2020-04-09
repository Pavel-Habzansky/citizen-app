package com.pavelhabzansky.domain.features.connectivity.manager

import android.content.BroadcastReceiver
import android.content.Context

interface IConnectivityManager {

    fun createNetworkReceiver(callback: (Boolean) -> Unit): BroadcastReceiver

    fun isConnected(): Boolean

}