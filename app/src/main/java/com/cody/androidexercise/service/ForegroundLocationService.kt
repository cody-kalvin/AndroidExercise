package com.cody.androidexercise.service

import android.app.*
import android.content.*
import android.content.res.Configuration
import android.location.Location
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cody.androidexercise.MainActivity
import com.cody.androidexercise.R
import com.cody.androidexercise.util.SharedPrefUtil
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class ForegroundLocationService : Service() {
    private var configurationChange = false

    private var serviceRunningInForeground = false

    private val binder = MyBinder()

    private lateinit var notificationManager: NotificationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private var currentLocation: Location? = null

    inner class MyBinder : Binder() {
        val service: ForegroundLocationService = this@ForegroundLocationService
    }

    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                locationResult?.lastLocation?.let { currentLocation ->
                    val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                    intent.putExtra(EXTRA_LOCATION, currentLocation)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                    if (serviceRunningInForeground) {
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            generateNotification(currentLocation)
                        )
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification) {
            unsubscribeToLocationUpdates()
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return binder
    }

    override fun onRebind(intent: Intent) {
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        if (!configurationChange && SharedPrefUtil.locationTracking) {
            val notification = generateNotification(currentLocation)
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }

        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    fun subscribeToLocationUpdates() {
        SharedPrefUtil.locationTracking = true

        startService(Intent(applicationContext, ForegroundLocationService::class.java))

        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } catch (error: SecurityException) {
            SharedPrefUtil.locationTracking = false
        }
    }

    fun unsubscribeToLocationUpdates() {
        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    stopSelf()
                }
            }
            SharedPrefUtil.locationTracking = false
        } catch (error: SecurityException) {
            SharedPrefUtil.locationTracking = true
        }
    }

    private fun generateNotification(location: Location?): Notification {
        val title = getString(R.string.app_name)

        val body = generateNotificationBody(location)

        generateNotificationChannel(title)

        val style = generateNotificationStyle(title, body)

        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID).run {
            setStyle(style)
            setContentTitle(title)
            setContentText(body)
            setSmallIcon(R.mipmap.ic_launcher)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setOngoing(true)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            generateNotificationLaunchAction(this@run)
            generateNotificationCancelAction(this@run)
        }

        return builder.build()
    }

    private fun generateNotificationBody(location: Location?): String {
        return if (location != null) {
            getString(R.string.display_coordinates, location.latitude, location.longitude)
        } else {
            getString(R.string.message_no_location_tracking)
        }
    }

    private fun generateNotificationChannel(title: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun generateNotificationStyle(
        title: String,
        body: String
    ): NotificationCompat.BigTextStyle {
        return NotificationCompat.BigTextStyle().run {
            setBigContentTitle(title)
            bigText(body)
        }
    }

    private fun generateNotificationLaunchAction(builder: NotificationCompat.Builder): NotificationCompat.Builder {
        val launchIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0)

        builder.addAction(
            android.R.drawable.ic_dialog_info,
            getString(R.string.action_start),
            pendingIntent
        )

        return builder
    }

    private fun generateNotificationCancelAction(builder: NotificationCompat.Builder): NotificationCompat.Builder {
        val cancelIntent = Intent(this, ForegroundLocationService::class.java).apply {
            putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)
        }

        val pendingIntent = PendingIntent.getService(
            this,
            0,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        builder.addAction(
            android.R.drawable.ic_delete,
            getString(R.string.action_cancel),
            pendingIntent
        )

        return builder
    }

    companion object {
        private const val PACKAGE_NAME = "com.cody.androidexercise.service"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

        private const val NOTIFICATION_ID = 123456789

        private const val NOTIFICATION_CHANNEL_ID = "foreground_location_channel_01"
    }
}