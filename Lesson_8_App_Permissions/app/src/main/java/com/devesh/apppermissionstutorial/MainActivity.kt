package com.devesh.apppermissionstutorial

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionDemoScreen()
        }
    }
}

@Composable
fun PermissionDemoScreen() {
    val context = LocalContext.current
    // 1. We need the Activity to check for "Rationale"
    val activity = context as ComponentActivity

    var isGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // 2. shouldShowRequestPermissionRationale returns true 
    // ONLY if the user has denied the permission at least once before.
    val showRationale by remember {
        mutableStateOf(ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.CAMERA)
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            isGranted = granted
            if (granted) {
                Toast.makeText(context, "Permission Granted!",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (isGranted) "Camera Permission: GRANTED" else "Camera Permission: DENIED")

        // 3. If the user denied it once, we show a message explaining why we need it
        if (showRationale) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "We need the camera to scan codes. Please allow it.")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (!isGranted) {
                launcher.launch(Manifest.permission.CAMERA)
            } else {
                Toast.makeText(context, "Already have permission!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Request Camera Permission")
        }
    }
}