package ru.vladkempo.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService: JobService(){

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onStartJob(p0: JobParameters?): Boolean {
        log("onStartJob")
        scope.launch {
            for (page in 0 until 10) {
                for (i in 0 until 5) {
                    delay(1000)
                    log("timer $i $page")

                }
            }

            jobFinished(p0, true)
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        log("onStopJob")
        return true
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



    private fun log(message: String) {
        Log.d("MyJobService", "MyJobService: $message")
    }

    companion object {
        const val ID = 222
    }


}