package com.devesh.myfileio

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class ExternalFileStorageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScopedStorageUI()
        }
    }
}

@Composable
fun ScopedStorageUI() {
    val context = LocalContext.current
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("No content yet") }
    val fileName = "My_New_File.txt"

    val contentResolver=context.contentResolver

    Column(Modifier.padding(20.dp).fillMaxSize()) {
        TextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Enter text to save...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Button(onClick = { saveFile(context, fileName, input) }, modifier = Modifier.fillMaxWidth()) {
            Text("Save to Downloads")
        }


        Spacer(Modifier.height(20.dp))

        Button(onClick = { output = readFile(context, fileName) ?: "File not found" }, modifier = Modifier.fillMaxWidth()) {
            Text("Read from Downloads")
        }

        Spacer(Modifier.height(10.dp))


        Text(text = "File Content: $output", style = MaterialTheme.typography.bodyLarge)
    }
}

// Logic to Save File
private fun saveFile(context: Context, name: String, content: String) {
    val contentResolver=context.contentResolver

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return

    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
    }

    val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
    uri?.let {
        contentResolver.openOutputStream(it)?.use { stream ->
            stream.write(content.toByteArray())
        }
        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
    }
}

// Logic to Read File
private fun readFile(context: Context, name: String): String? {


    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return null

    val contentResolver = context.contentResolver
    val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
    val args = arrayOf(name)

    val uri = contentResolver.query(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI, 
        arrayOf(MediaStore.MediaColumns._ID), 
        selection, args, null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(
                MediaStore.MediaColumns._ID))
            ContentUris.withAppendedId(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI, id)
        } else null
    }

    return uri?.let { contentResolver.openInputStream(it)?.bufferedReader()?.readText() }
}
