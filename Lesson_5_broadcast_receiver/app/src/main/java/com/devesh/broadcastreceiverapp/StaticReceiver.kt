package com.devesh.broadcastreceiverapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * A Static BroadcastReceiver is declared in the AndroidManifest.xml.
 *
 * Historically, these could wake up an app even if it wasn't running.
 * However, starting with Android 8.0 (API 26), most implicit broadcasts
 * (like BATTERY_LOW) cannot be received by static receivers to save battery.
 *
 * Custom broadcasts (like this one) still work if they are "explicit"
 * (sent to a specific package).
 */
class StaticReceiver : BroadcastReceiver() {

    /**
     * This method is triggered when a broadcast matching the intent-filter 
     * in the AndroidManifest is sent.
     * 
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received, containing the action and data.
     */
    override fun onReceive(context: Context, intent: Intent) {
        
        // Extracting the data we passed in the Intent from MainActivity.
        // We use the same key "EXTRA_MSG" that was used when sending the broadcast.
        val message = intent.getStringExtra("EXTRA_MSG") ?: "No Message Received"
        
        // Logging the message for debugging purposes (visible in Logcat).
        Log.d("StaticReceiver", "onReceive: Static Receiver: $message ")
        
        // Displaying a Toast to visually confirm the broadcast was received.
        Toast.makeText(context,
            "Static Receiver: $message",
            Toast.LENGTH_LONG).show()
    }
}
