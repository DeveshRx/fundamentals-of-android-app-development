package com.devesh.bind_service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.bind_service.ui.theme.HelloWorldAppTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private var mService: MyBoundService? = null
    private var mBound by mutableStateOf(false)
    private var randomNumber by mutableStateOf(0)

    /**
     * ServiceConnection acts as the bridge between the Activity and the Service.
     * It listens for when the service is connected or disconnected.
     */
    private val connection = object : ServiceConnection {

        /**
         * Called when the connection with the service has been established.
         * @param service The IBinder of the communication channel to the Service.
         */
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected called")
            // We've bound to MyBoundService, cast the IBinder and get MyBoundService instance
            val binder = service as MyBoundService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        /**
         * Called when the connection with the service has been unexpectedly disconnected.
         * This is NOT called when the client unbinds.
         */
        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(TAG, "onServiceDisconnected called")
            mBound = false
            mService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        setContent {
            HelloWorldAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BoundServiceScreen(
                        modifier = Modifier.padding(innerPadding),
                        isBound = mBound,
                        number = randomNumber,
                        onGetNumberClick = {
                            if (mBound) {
                                // Interaction: Calling a public method on the bound service
                                randomNumber = mService?.getRandomNumber() ?: 0
                                Log.d(TAG, "Button clicked: Received $randomNumber from service")
                            } else {
                                Log.d(TAG, "Button clicked: Service not bound")
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Binding to service...")
        // Bind to LocalService
        Intent(this, MyBoundService::class.java).also { intent ->
            // Context.BIND_AUTO_CREATE: Automatically create the service as long as the binding exists
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Unbinding from service...")
        // Unbind from the service to avoid memory leaks
        if (mBound) {
            unbindService(connection)
            mBound = false
        }
    }
}

/**
 * UI for displaying the bound status and interacting with the service.
 */
@Composable
fun BoundServiceScreen(
    modifier: Modifier = Modifier,
    isBound: Boolean,
    number: Int,
    onGetNumberClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isBound) "Service Bound" else "Service Unbound",
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Random Number: $number", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onGetNumberClick,
            enabled = isBound // Button is only active when service is bound
        ) {
            Text("Get Number from Service")
        }
    }
}
