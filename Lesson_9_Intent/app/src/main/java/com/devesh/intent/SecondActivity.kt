package com.devesh.intent

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Get the data that was sent to us
        val receivedName = intent.getStringExtra("USER_NAME") ?: "No Name"

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SecondScreen(
                        name = receivedName,
                        onBackClick = { finish() },
                        onSendResultClick = {
                            // 2. Create an intent just to hold data
                            val dataToReturn = Intent()
                            dataToReturn.putExtra("MY_RESULT_KEY",
                                "Hello from Second Activity!")
                            
                            // 3. Set the result and close
                            setResult(RESULT_OK, dataToReturn)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SecondScreen(name: String, onBackClick: () -> Unit, onSendResultClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Second Activity", style = MaterialTheme.typography.headlineMedium)
        Text("Received Name: $name")
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Button(onClick = onSendResultClick) { Text("Send Data Back to Main") }
        Button(onClick = onBackClick) { Text("Just Go Back") }
    }
}
