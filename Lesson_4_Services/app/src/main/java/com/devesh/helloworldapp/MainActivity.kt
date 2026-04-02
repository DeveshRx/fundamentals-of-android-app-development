package com.devesh.helloworldapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devesh.helloworldapp.ui.theme.HelloWorldAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloWorldAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Background Service Tutorial")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            /** Start MyService Service */
            val intent = Intent(context, MyService::class.java)
            context.startService(intent)
        }) {
            Text(text = "Start  Service")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            /** Stop MyService Service */
            val intent = Intent(context, MyService::class.java)
            context.stopService(intent)
        }) {
            Text(text = "Stop Service")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    HelloWorldAppTheme {
        MainScreen()
    }
}
