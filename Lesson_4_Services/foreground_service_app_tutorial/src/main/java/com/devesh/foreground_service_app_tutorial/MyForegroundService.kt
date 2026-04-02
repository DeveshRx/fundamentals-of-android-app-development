package com.devesh.foreground_service_app_tutorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MyForegroundService : Service() {

    private val TAG = "MyForegroundService"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service Created")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind called")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: action = ${intent?.action}")
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> {
                Log.d(TAG, "Stopping service via Intent")
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        Log.d(TAG, "Starting foreground service...")

        // Create Notification
        val channelId = "running_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Running Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created")
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is active")
            .setContentText("Foreground service is running...")
            .build()

        // Start Foreground Service with Notification
        startForeground(1, notification)
        Log.d(TAG, "Foreground notification posted")

        // START YOUR TASK HERE ---
        myTask()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Destroyed")
        loggingJob?.cancel()
        Log.d(TAG, "Background task Stopped")
    }

    enum class Actions {
        START, STOP
    }



    private var loggingJob: Job? = null

    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    fun myTask() {
        // Task: Start periodic logging if it hasn't started yet
        if (loggingJob == null) {
            Log.d(TAG, "Starting periodic task...")
            loggingJob = serviceScope.launch {
                while (isActive) {
                    Log.d(TAG, "Service Status: Running smoothly...")
                    delay(3000) // Log every 3 seconds
                }
            }
        }
    }

}
