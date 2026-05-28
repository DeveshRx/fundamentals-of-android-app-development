package com.devesh.myfileio

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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

class FileSaveInternalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileSaveScreen()
        }
    }
}

@Composable
fun FileSaveScreen() {

    Column(modifier = Modifier.padding(20.dp)) {

        SaveFile()
        ReadFromFile()
        ListofAllFiles()
    }


}

@Composable
fun SaveFile() {

    val context = LocalContext.current
    var userInput by remember { mutableStateOf("") }

    TextField(
        value = userInput,
        onValueChange = { userInput = it },
        placeholder = { Text("Write something to save...") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(10.dp))

    Button(onClick = {
        if (userInput.isNotEmpty()) {
            val filename = "HelloWorld.txt"

            // Core File IO Logic
            val fileOutputStream = context.openFileOutput(filename, MODE_PRIVATE)

            fileOutputStream.use { output ->
                output.write(userInput.toByteArray())
            }

            Toast.makeText(context, "File Saved!", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text("Save to Internal Storage")
    }
}

@Composable
fun ReadFromFile() {
    val context = LocalContext.current

    var readContent by remember { mutableStateOf("") }

    Spacer(modifier = Modifier.height(10.dp))

    Button(onClick = {
        val filename = "HelloWorld.txt"
        try {
            val fileInputStream = context.openFileInput(filename)

            fileInputStream.bufferedReader().use {
                readContent = it.readText()
            }
            Toast.makeText(context, "File Read Success!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error reading file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text("Read from Internal Storage")
    }

    if (readContent.isNotEmpty()) {
        Spacer(modifier = Modifier.height(10.dp))

            Text(text = "File Content: $readContent")


    }
}

@Composable
fun ListofAllFiles() {
    val context = LocalContext.current
    var fileList by remember { mutableStateOf(emptyList<String>()) }

    Spacer(modifier = Modifier.height(10.dp))

    Button(onClick = {
        fileList = context.fileList().toList()
    }) {
        Text("List All Files")
    }

    fileList.forEach { fileName ->
        Text(text = fileName, modifier = Modifier.padding(top = 4.dp))
    }
}