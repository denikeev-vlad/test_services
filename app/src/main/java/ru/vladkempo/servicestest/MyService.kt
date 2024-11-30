package ru.vladkempo.servicestest

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService: android.app.Service(){

    private val scope = CoroutineScope(Dispatchers.Main)
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        log("onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        scope.launch {
            for (i in 0 until 100) {
                delay(1000)
                log("timer $i")

            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun log(message: String) {
        Log.d("SERVICE", "MyService: $message")
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MyService::class.java)
        }
    }
}