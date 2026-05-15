package com.devesh.dbtutoriall

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.devesh.dbtutoriall.database.Flower
import com.devesh.dbtutoriall.database.FlowerDao
import com.devesh.dbtutoriall.database.FlowerDatabase
import com.devesh.dbtutoriall.ui.theme.DBTutoriallTheme
import kotlinx.coroutines.launch

/**
 * UPDATE ACTIVITY: This activity allows users to modify an existing flower.
 * You must provide the correct ID of the flower you want to change.
 */
class EditDatabaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize Database and DAO
        val db = FlowerDatabase.getDatabase(this)
        val flowerDao = db.flowerDao()

        setContent {
            DBTutoriallTheme {
                Scaffold { innerPadding ->
                    EditFlowerScreen(flowerDao, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun EditFlowerScreen(flowerDao: FlowerDao, modifier: Modifier = Modifier) {
    // State variables for inputs
    var idInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var colorInput by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier.padding(16.dp).fillMaxWidth()) {
        Text("Update Flower Entry", style = MaterialTheme.typography.headlineMedium)

        // The ID is crucial because Room uses it to find the specific row to update
        OutlinedTextField(
            value = idInput,
            onValueChange = { idInput = it },
            label = { Text("Flower ID (UID)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("New Flower Name") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = colorInput,
            onValueChange = { colorInput = it },
            label = { Text("New Flower Color") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        // UPDATE OPERATION
        Button(
            onClick = {
                val id = idInput.toIntOrNull()
                if (id != null && nameInput.isNotBlank() && colorInput.isNotBlank()) {
                    scope.launch {
                        /**
                         * IMPORTANT: We create a flower object with an EXISTING ID.
                         * When we call updateFlower(), Room sees the ID and knows 
                         * to overwrite that specific row.
                         */

                        val updatedFlower = Flower(id = id,
                            name = nameInput,
                            color = colorInput)
                        flowerDao.updateFlower(updatedFlower)
                        
                    }
                    Toast.makeText(context, "Flower Updated!", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "Please enter valid ID and Details", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Text("Update Record")
        }
    }
}
