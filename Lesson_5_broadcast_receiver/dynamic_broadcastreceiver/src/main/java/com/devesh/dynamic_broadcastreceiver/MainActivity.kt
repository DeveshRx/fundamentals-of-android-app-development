package com.devesh.dynamic_broadcastreceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.dynamic_broadcastreceiver.ui.theme.BroadcastReceiverAppTheme

/**
 * This Activity demonstrates a Dynamic BroadcastReceiver.
 * Unlike static receivers, dynamic receivers are registered and unregistered 
 * programmatically during the app's lifecycle (e.g., in onStart/onStop).
 */
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge display.
        enableEdgeToEdge()
        setContent {
            BroadcastReceiverAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Displaying the current state text.
                        Text(text = receivedText, fontSize = 20.sp)
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(onClick = {
                            sendEvent()
                        }) {
                            Text("Send Broadcast")
                        }
                    }
                }
            }
        }
    }

    // 1. Define a unique action string for the custom broadcast.
    private val CUSTOM_ACTION = "com.devesh.ACTION_SEND"

    // 2. State variable to hold the message received from the broadcast.
    // Using 'mutableStateOf' allows Compose to automatically recompose the UI when the value changes.
    private var receivedText by mutableStateOf("Waiting for broadcast...")

    // 3. Define the BroadcastReceiver as an anonymous object.
    private val receiver = object : BroadcastReceiver() {
        /**
         * This method is called when the broadcast with CUSTOM_ACTION is received.
         */
        override fun onReceive(context: Context?, intent: Intent?) {

                // Extracting data passed in the Intent.
                val data = intent?.getStringExtra("extra_data") ?: "No data"

                // Updating the state, which triggers a UI refresh.
                receivedText = "Broadcast Received: $data"

        }
    }


    fun sendEvent() {
        // 6. Sending a custom broadcast within the same app.
        // We create an Intent with our custom action and add some extra data.
        val intent = Intent(CUSTOM_ACTION).apply {
            putExtra("extra_data", "Hello from Dynamic Receiver!")
        }
        sendBroadcast(intent)
    }
    /**
     * 4. Register the receiver in onStart.
     * This means the receiver will only be active when the Activity is visible.
     */
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        
        // IntentFilter defines which broadcasts this receiver is interested in.
        val filter = IntentFilter(CUSTOM_ACTION)
        
        // Starting from Android 13 (Tiramisu), you must specify if the receiver 
        // is exported to other apps or kept private (RECEIVER_EXPORTED or RECEIVER_NOT_EXPORTED).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, RECEIVER_EXPORTED)
        } else {
            // For older versions, standard registration is used.
            registerReceiver(receiver, filter)
        }
    }

    /**
     * 5. Unregister the receiver in onStop.
     * It is crucial to unregister dynamic receivers to prevent memory leaks 
     * and ensuring the app doesn't try to update the UI when it's not in the foreground.
     */
    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }
}
