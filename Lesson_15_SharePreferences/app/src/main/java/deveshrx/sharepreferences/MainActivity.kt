package deveshrx.sharepreferences

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A simple surface to host our demo
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                SharedPreferencesDemo()
            }
        }
    }
}

@Composable
fun SharedPreferencesDemo() {
    val context = LocalContext.current

    // State variables to hold user input and results
    var inputKey by remember { mutableStateOf("") }
    var inputValue by remember { mutableStateOf("") }
    var searchKey by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    fun writeValue(){
        val pref =context
            .getSharedPreferences("my_user_data",Context.MODE_PRIVATE)
        if (inputKey.isNotEmpty()) {
            // Save data using .edit { }
            pref.edit { putString(inputKey,
                inputValue)
            }
        }
        Log.d("TAG", "writeValue: Saved ${inputKey}")
        Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show()

    }

    fun readValue(): String? {
        val pref =context.getSharedPreferences("my_user_data", Context.MODE_PRIVATE)
        val data = pref.getString(searchKey, "Not Found")
        return data
    }

    fun deleteValue(){
        val pref =context.getSharedPreferences("my_user_data", Context.MODE_PRIVATE)
        if (searchKey.isNotEmpty()) {
            // Save data using .edit { }
            pref.edit {
                remove(searchKey)
            }
        }
        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()), // Makes the screen scrollable
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text("SharedPreferences Demo", style = MaterialTheme.typography.headlineMedium)

        // --- WRITE SECTION ---
        Text("Save Data", style = MaterialTheme.typography.titleLarge)
        TextField(value = inputKey, onValueChange = { inputKey = it }, label = { Text("Key") })
        TextField(value = inputValue, onValueChange = { inputValue = it }, label = { Text("Value") })
        
        Button(onClick = {
           writeValue()
        }) {
            Text("Save")
        }

        HorizontalDivider()


        Text("Load Data", style = MaterialTheme.typography.titleLarge)

        TextField(value = searchKey, onValueChange = { searchKey = it }, label = { Text("Enter Key to Load") })

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {

            Button(onClick = {
                // Read data (returns "Not Found" if key doesn't exist)
                val data = readValue()
                resultText = "Result: $data"
            }) {
                Text("Read")
            }
            Button(onClick = {
                // Delete Value
                deleteValue()
             }) {
                Text("Delete")
            }
        }


        if (resultText.isNotEmpty()) {
            Text(resultText, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}
