package ru.vladkempo.servicestest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import ru.vladkempo.servicestest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("MainActivity", "onCreate")

        binding.simpleService.setOnClickListener {
            startService(MyService.newIntent(this))
        }

        binding.foregroundService.setOnClickListener {
            Log.d("MainActivity", "foregroundService clicked")
            Toast.makeText(this, "foregroundService", Toast.LENGTH_SHORT).show()
            ContextCompat.startForegroundService(this, MyForegroundService.newIntent(this))
//            showNotification()
//            requestNotificationPermission()
        }
        binding.intentService.setOnClickListener {
            startService(MyIntentService.newIntent(this))
        }
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)

            val jobInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31 (Android 12)
                val networkRequest = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) // Неметримая сеть
                    .build()

                JobInfo.Builder(MyJobService.ID, componentName)
                    .setRequiredNetwork(networkRequest) // Новый метод для Android 12+
                    .setRequiresCharging(true)
                    .setPersisted(true)
                    .build()
            } else {
                // Для Android ниже API 31
                JobInfo.Builder(MyJobService.ID, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setRequiresCharging(true)
                    .setPersisted(true)
                    .build()
            }

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode = jobScheduler.schedule(jobInfo)
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Toast.makeText(this, "Job успешно запланирован", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ошибка планирования Job", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setSmallIcon(R.drawable.i)
            .setContentText("Content")
            .build()
        notificationManager.notify(id++, notification)
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
//
//    private fun showNotification() {
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(
//                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("Title")
//            .setContentText("Text")
//            .setSmallIcon(R.color.black)
//            .build()
//        notificationManager.notify(id++, notification)
//    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        var id = 1
    }

}