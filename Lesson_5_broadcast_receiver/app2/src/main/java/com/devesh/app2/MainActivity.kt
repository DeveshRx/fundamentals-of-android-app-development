package com.devesh.app2

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.devesh.app2.ui.theme.BroadcastReceiverAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge display, allowing content to draw behind system bars (status/navigation bars)
        enableEdgeToEdge()
        setContent {
            // Applying the custom theme defined for the application
            BroadcastReceiverAppTheme {
                // Scaffold provides the standard Material Design layout structure
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Calling our custom screen and passing the padding provided by Scaffold
                    AirplaneModeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * A Composable function that handles the logic and UI for monitoring Airplane Mode.
 * It demonstrates how to register and unregister a dynamic BroadcastReceiver in Compose.
 */
@Composable
fun AirplaneModeScreen(modifier: Modifier = Modifier) {
    // Accessing the current context to register the receiver
    val context = LocalContext.current
    // Creating an instance of our  AirplaneModeReceiver
    val receiver = AirplaneModeReceiver()

    // registering a receiver).
    DisposableEffect(context) {
        // IntentFilter specifies the 'actions' the receiver should listen for.
        // Here we listen for the system-wide 'Airplane Mode Changed' event.
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)

        // Dynamically registering the receiver with the filter
        context.registerReceiver(receiver, filter)

        // onDispose is called when the Composable leaves the screen.
        // It is critical to unregister the receiver here to avoid memory leaks
        // and unnecessary processing when the UI is no longer active.
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    // A simple UI to inform the user that monitoring is active
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // Centers children vertically in the column
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        Text(text = "Monitoring Airplane Mode")
    }
}
