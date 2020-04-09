package com.pavelhabzansky.domain.features.connectivity.usecase

import android.content.BroadcastReceiver
import com.pavelhabzansky.domain.core.SimpleUseCase
import com.pavelhabzansky.domain.features.connectivity.manager.IConnectivityManager

class CreateConnectionReceiverUseCase(
        private val connectivityManager: IConnectivityManager
) : SimpleUseCase<BroadcastReceiver, (Boolean) -> Unit>() {

    override fun doWork(params: (Boolean) -> Unit): BroadcastReceiver {
        return connectivityManager.createNetworkReceiver(params)
    }


}