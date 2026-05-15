package com.devesh.dbtutoriall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devesh.dbtutoriall.database.Flower
import com.devesh.dbtutoriall.database.FlowerDatabase
import com.devesh.dbtutoriall.ui.theme.DBTutoriallTheme

/**
 * READ ACTIVITY: This activity displays all entries currently in the database.
 * It observes the database using Flow, so it updates automatically if the data changes.
 */
class ViewAllDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Database and DAO
        val db = FlowerDatabase.getDatabase(this)
        val flowerDao = db.flowerDao()

        enableEdgeToEdge()
        setContent {
            DBTutoriallTheme {
                /**
                 * REAL-TIME UPDATES: We collect the Flow from getAllFlowers() as Compose State.
                 * This means as soon as a flower is added or deleted elsewhere, 
                 * this list will refresh automatically!
                 */
                val flowers by flowerDao.getAllFlowers().collectAsState(initial = emptyList())

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlowerList(
                        flowers = flowers,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * A scrollable list (LazyColumn) of flowers.
 */
@Composable
fun FlowerList(flowers: List<Flower>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(flowers) { flower ->
            FlowerItem(flower)
        }
    }
}

/**
 * Individual card displaying one flower's details.
 */
@Composable
fun FlowerItem(flower: Flower) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // We display the ID so students can use it for Update/Delete operations
            Text(text = "ID (UID): ${flower.id}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Name: ${flower.name}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Color: ${flower.color}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
