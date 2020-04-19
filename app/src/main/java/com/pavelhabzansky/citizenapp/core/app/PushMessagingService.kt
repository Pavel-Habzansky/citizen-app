package com.pavelhabzansky.citizenapp.core.app

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pavelhabzansky.domain.features.events.domain.PushEvent
import com.pavelhabzansky.domain.features.events.usecase.StorePushEventUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PushMessagingService : FirebaseMessagingService() {

    private val storePushEvent by inject<StorePushEventUseCase>()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val id = message.messageId
        val title = message.data["title"]
        val body = message.data["body"]
        val url = message.data["url"]

        if (id != null && title != null && body != null) {
            val pushEvent = PushEvent(id, title, body, System.currentTimeMillis(), url)

            GlobalScope.launch(Dispatchers.IO) {
                storePushEvent(pushEvent)
            }
        }

    }

}