package com.devesh.app2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * A BroadcastReceiver is a component that allows the system or other apps to deliver 
 * events to your app. Here, it listens for changes in Airplane Mode.
 */
class AirplaneModeReceiver : BroadcastReceiver() {

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * 
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received, containing the action and optional data.
     */
    override fun onReceive(context: Context?, intent: Intent?) {

            
            // Extracting the boolean extra "state" from the intent.
            // "state" is a standard key used by the system to indicate the new status of airplane mode.
            // We provide 'false' as a default value if the extra is not found.
            val isAirplaneModeOn = intent?.getBooleanExtra("state", false)
            
            // Preparing a message based on whether airplane mode is turned on or off.
            val message = if (isAirplaneModeOn == true) "Airplane Mode ON" else "Airplane Mode OFF"
            
            // Displaying a short-lived pop-up message (Toast) to the user.
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            Log.d("AirplaneModeReceiver", "onReceive: "+message)

    }
}
