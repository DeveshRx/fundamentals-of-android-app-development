package com.devesh.notifications

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devesh.notifications.ui.theme.NotificationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Create the Notification Channel (Required for Android 8.0+)
        // Notifications on Android 8.0 and higher must be assigned to a channel.
        createNotificationChannel()
        
        // Enable edge-to-edge display for modern UI look
        enableEdgeToEdge()
        
        setContent {
            NotificationsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Main UI content
                    NotificationDemo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    /**
     * Creates a notification channel which is mandatory for Android 8.0 (API level 26) and higher.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My App Notification Channel"
            val descriptionText = "This is a demo notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            // Defining the channel with a unique ID
            val channel = NotificationChannel("MY_CHANNEL_ID", name, importance)
                .apply {
                    description = descriptionText
                }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds and displays a simple notification.
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showSimpleNotification(context: Context) {
        // Create an Intent for the activity to open when notification is clicked
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Create a PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE // FLAG_IMMUTABLE is required for security on modern Android
        )

        // Build the notification
        val builder = NotificationCompat.Builder(context, "MY_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_dialog_info) // Using a built-in icon
            .setContentTitle("Demo Notification")
            .setContentText("Hello! This is a simple Android notification.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Set the action when tapped
            .setAutoCancel(true) // Automatically remove the notification when tapped

        // Show the notification using NotificationManagerCompat
        NotificationManagerCompat.from(context).notify(1, builder.build())
    }

    /**
     * Composable function that handles the UI and permission logic for showing notifications.
     */
    @Composable
    fun NotificationDemo(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        
        // Check if the app has permission to post notifications (Required for Android 13+)
        var hasNotificationPermission by remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mutableStateOf(
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                )
            } else {
                // For versions below Android 13, permission is granted at install time
                mutableStateOf(true)
            }
        }

        // Launcher for requesting notification permission
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasNotificationPermission = isGranted
            })

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                if (hasNotificationPermission) {
                    // Show notification if permission is already granted
                    showSimpleNotification(context)
                } else {
                    // Request permission if not granted (Android 13+)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }) {
                Text(text = "Show Notification")
            }
        }
    }



}
