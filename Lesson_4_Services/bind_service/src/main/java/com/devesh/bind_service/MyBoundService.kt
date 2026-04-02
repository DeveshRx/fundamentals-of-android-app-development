package com.devesh.bind_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlin.random.Random

class MyBoundService : Service() {

    companion object {
        private const val TAG = "MyBoundService"
    }

    // Binder given to clients
    private val binder = LocalBinder()

    // Random number generator
    private val mGenerator = Random

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of MyBoundService so clients can call public methods
        fun getService(): MyBoundService = this@MyBoundService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
    }

    /**
     * Called when a client (like an Activity) calls bindService().
     * This is where we return our Binder object to the client.
     */
    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "Service onBind")
        return binder
    }

    /**
     * Called when all clients have disconnected from the service.
     */
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "Service onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service onDestroy")
    }

    /** Method for clients to call to get a random number */
    fun getRandomNumber(): Int {
        val num = mGenerator.nextInt(100)
        Log.d(TAG, "Generating random number: $num")
        return num
    }
}
