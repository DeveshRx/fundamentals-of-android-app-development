package com.devesh.contentprovider

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

/**
 * MainActivity demonstrating how to use Content Providers to read data from other apps.
 * In this case, we are reading contacts from the system's Contacts app.
 */
class MainActivity : ComponentActivity() {

    // State variable to track if the READ_CONTACTS permission is granted.
    // mutableStateOf allows Compose to re-render when the value changes.
    private var hasPermission by mutableStateOf(false)

    // Activity Result Launcher for requesting permissions.
    // This is the modern way to handle runtime permissions.
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Update our state when the user responds to the permission dialog
        hasPermission = isGranted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request permission immediately when the app starts.
        checkAndRequestPermission()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Pass the current permission status to the Composable UI.
                    ContactScreen(hasPermission)
                }
            }
        }
    }

    /**
     * Checks if the app already has permission. If not, launches the request dialog.
     */
    private fun checkAndRequestPermission() {
        val permission = Manifest.permission.READ_CONTACTS
        val isGranted = ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
        
        hasPermission = isGranted

        if (!isGranted) {
            // This triggers the system permission dialog.
            requestPermissionLauncher.launch(permission)
        }
    }
}

@Composable
fun ContactScreen(hasPermission: Boolean) {
    val context = LocalContext.current
    
    // mutableStateListOf is a special list that notifies Compose when items are added/removed.
    // remember ensures the list isn't recreated during every recomposition.
    val contactList = remember { mutableStateListOf<String>() }

    fun LoadContent(){
        /**
         * CONTENT PROVIDER LOGIC:
         * 1. Get the ContentResolver. It acts as a client for Content Providers.
         * 2. Specify the URI (Uniform Resource Identifier) for the data you want.
         * 3. Perform a query.
         */
        val resolver = context.contentResolver
        val uri = ContactsContract.Contacts.CONTENT_URI // Standard URI for contacts
        // example: content://contacts

        // resolver.query() is similar to a SQL SELECT statement.
        // It returns a Cursor object which points to the resulting rows.
        val cursor = resolver.query(uri,
            null,
            null,
            null,
            null)

        // Use 'use' to ensure the Cursor is closed properly even if an exception occurs.
        cursor?.use {
            contactList.clear() // Clear existing list before reloading.

            // Find the index of the column we are interested in.
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            if (nameIndex != -1) {
                // Iterate through the results row by row.
                while (it.moveToNext()) {
                    // Extract the string value from the specific column index.
                    val name = it.getString(nameIndex)
                    contactList.add(name)
                }
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (!hasPermission) {
            // Inform the user if permission was denied.
            Text(
                text = "Contact permission is required to use this app. Please enable it in settings.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            // UI when permission is granted.
            Button(onClick = {
         LoadContent()
            }) {
                Text("Load Contacts using Provider")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Efficiently display the list of contacts.
            LazyColumn {
                items(contactList) { name ->
                    Text(text = "👤 $name", modifier = Modifier.padding(8.dp))
                    HorizontalDivider()
                }
            }
        }
    }
}

