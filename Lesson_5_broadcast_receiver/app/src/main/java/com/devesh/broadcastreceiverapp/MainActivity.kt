package com.devesh.broadcastreceiverapp

import android.content.Intent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.devesh.broadcastreceiverapp.ui.theme.BroadcastReceiverAppTheme

/**
 * A BroadcastReceiver is an Android component that listens for system-wide or app-specific events.
 *
 * This tutorial covers:
 * 1. Static Registration (Manifest-declared): Registered in AndroidManifest.xml.
 * 2. Sending Broadcasts: Triggering events with Intents.
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge display, allowing content to draw behind system bars.
        enableEdgeToEdge()
        setContent {
            // Applying the application's theme
            BroadcastReceiverAppTheme {
                // Standard Material Design layout container
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Main UI screen for the tutorial
                    BroadcastTutorialScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Main UI component for triggering broadcasts.
 */
@Composable
fun BroadcastTutorialScreen(modifier: Modifier = Modifier) {
    // Accessing current Context in Compose
    val context = LocalContext.current

    // Define a unique action string for our custom broadcast.
    // Usually follows the package naming convention to avoid conflicts.
    val staticAction = "com.devesh.broadcastreceiverapp.STATIC_ACTION"

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // Centering elements vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centering elements horizontally
    ) {
        Text(
            text = "Broadcast Receiver Tutorial",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Button to trigger the custom broadcast
        Button(onClick = {
            // SENDING A STATIC BROADCAST
            // 1. Create an Intent with the custom action string.
            val intent = Intent(staticAction).apply {
                // 2. Add extra data to the intent (Key-Value pairs).
                putExtra("EXTRA_MSG", "Hello! Static event triggered.")
                
                // 3. IMPORTANT: For implicit broadcasts on Android 8.0+ (Oreo), 
                // you must specify the package name to target your own app.
                // This is called an Explicit Broadcast.
                setPackage(context.packageName)
            }
            
            // 4. Send the broadcast to the system.
            context.sendBroadcast(intent)
        }) {
            Text(text = "Trigger Static Broadcast")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Static results will appear as a Toast notification",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
