package com.devesh.helloworldapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*

class MyService : Service() {
    private var loggingJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())

    override fun onCreate() {
        super.onCreate()
        Log.d("MyService", "Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService", "Service Started")
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()

        // Task: Start periodic logging if it hasn't started yet
        if (loggingJob == null) {
            loggingJob = serviceScope.launch {
                while (isActive) {
                    Log.d("MyService", "Service Status: Running smoothly...")
                    delay(3000) // Log every 3 seconds
                }
            }
        }
        
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the background logging when the service is destroyed
        serviceScope.cancel()
        Log.d("MyService", "Service Destroyed")
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}







