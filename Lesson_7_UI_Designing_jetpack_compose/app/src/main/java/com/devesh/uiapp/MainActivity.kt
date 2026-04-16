package com.devesh.uiapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devesh.uiapp.ui.theme.UIAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Preview
@Composable
fun MainScreen() {
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Add Main Content Here
                CardList()
            }
        }
    }
}


// 1. Simple Data Model
data class CardItem(val title: String, val summary: String, val imageRes: Int)

// 2. Sample Data
val sampleData = listOf(
    CardItem(
        "Modern Architecture",
        "Explore beautiful designs.",
        R.drawable.ic_android_24dp
    ),
    CardItem(
        "Nature Wonders",
        "Discover breathtaking landscapes.",
        R.drawable.ic_android_24dp
    ),
    CardItem("Tech Innovations", "Latest trends in AI.", R.drawable.ic_android_24dp)
)

// 3. The List Component
@Composable
fun CardList() {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(sampleData) { item ->
            ListItem(item)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// 4. The Individual Row Component
@Composable
fun ListItem(item: CardItem) {
    val context=LocalContext.current
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Text(text = item.summary, style = MaterialTheme.typography.bodySmall)
            }
        }
        Button(onClick = {
            Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
        }, modifier = Modifier
            .align(Alignment.End)
            .padding(5.dp)) {
            Text("Click Me")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CardList()
        }
    }
}
