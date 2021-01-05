package com.cody.androidexercise.service

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cody.androidexercise.util.SharedPrefUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
       message.notification?.let { notification ->
           broadcastNotificationBody(notification.body ?: "Wew")
       }
    }

    override fun onNewToken(token: String) {
        SharedPrefUtil.firebaseToken = token
    }

    private fun broadcastNotificationBody(data: String) {
        val intent = Intent(ACTION_FCM_DATA_BROADCAST)
        intent.putExtra(EXTRA_DATA, data)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    companion object {
        private const val PACKAGE_NAME = "com.cody.androidexercise.service"

        internal const val ACTION_FCM_DATA_BROADCAST = "${PACKAGE_NAME}.action.FCM_DATA_BROADCAST"

        internal const val EXTRA_DATA = "$PACKAGE_NAME.extra.DATA"
    }
}