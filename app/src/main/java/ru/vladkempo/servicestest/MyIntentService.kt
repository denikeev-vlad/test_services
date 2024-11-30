package ru.vladkempo.servicestest

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyIntentService: IntentService(SERVICE_NAME){


    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 100) {
            Thread.sleep(1000)
            Log.d("MyForegroundService", "timer $i")

        }
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true)
        requestNotificationPermission()
        startForeground(1, showNotification())

    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }


    private fun log(message: String) {
        Log.d("SERVICE", "MyForegroundService: $message")
    }


    private fun showNotification() : Notification {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setSmallIcon(R.drawable.i)
            .setContentText("Content")
            .build()
    }

    private fun requestNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
        } else {
            showNotification()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }

        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val SERVICE_NAME = "intent_service"
    }
}