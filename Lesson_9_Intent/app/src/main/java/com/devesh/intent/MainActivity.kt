package com.devesh.intent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {

    // 1. To get data back, we register a listener at the top of our class
    private var resultFromSecondActivity by mutableStateOf("Nothing yet")
    
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val returnedData = result.data?.getStringExtra("MY_RESULT_KEY")
            resultFromSecondActivity = returnedData ?: "Empty Result"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        resultText = resultFromSecondActivity,
                        onExplicitClick = {
                            // EXPLICIT: Move from A to B directly
                            val intent = Intent(this, SecondActivity::class.java)
                            intent.putExtra("USER_NAME", "Alice")
                            startActivity(intent)
                        },
                        onGetResultClick = {
                            // GET RESULT: Start activity and wait for data back
                            val intent = Intent(this, SecondActivity::class.java)
                            startForResult.launch(intent)
                        },
                        onImplicitClick = {
                            // IMPLICIT: Ask Android to open a link in any browser
                            val intent = Intent(Intent.ACTION_VIEW,
                                "https://google.com".toUri())
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    resultText: String,
    onExplicitClick: () -> Unit,
    onImplicitClick: () -> Unit,
    onGetResultClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Intent Tutorial", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onExplicitClick) { Text("Go to Second Activity") }
        Button(onClick = onImplicitClick) { Text("Open Google.com") }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text("Result from Second Activity: $resultText")
        Button(onClick = onGetResultClick) { Text("Get Result Back") }
    }
}
