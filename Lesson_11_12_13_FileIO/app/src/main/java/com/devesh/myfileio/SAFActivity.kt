package com.devesh.myfileio

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.devesh.myfileio.ui.theme.ui.theme.MyFileIOTheme

class SAFActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFileIOTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        SAFScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun SAFScreen() {
    val context = LocalContext.current
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("No content yet") }
    val fileName = "My_New_File.txt"

    val contentResolver = context.contentResolver


    // Launcher for Storage Access Framework (SAF)
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        uri?.let {
            contentResolver.openOutputStream(it)?.use { stream ->
                stream.write(input.toByteArray())
            }
            Toast.makeText(context, "Saved to selected location!", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher for Reading via SAF
    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            val content = contentResolver.openInputStream(it)?.bufferedReader()?.use { reader ->
                reader.readText()
            }
            output = content ?: "Error reading file"
        }
    }


    Column(Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        TextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Enter text to save...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Button(onClick = {
            createFileLauncher.launch(fileName)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Save to Other Folder")
        }


        Spacer(Modifier.height(10.dp))

        Button(onClick = {
            openFileLauncher.launch(arrayOf("text/plain"))
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Read from Other Folder")
        }

        Spacer(Modifier.height(10.dp))

        Text(text = "File Content: $output", style = MaterialTheme.typography.bodyLarge)
    }

}