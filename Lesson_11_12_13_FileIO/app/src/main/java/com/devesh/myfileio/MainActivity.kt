package com.devesh.myfileio

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devesh.myfileio.ui.theme.MyFileIOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFileIOTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                        ){

                        Button(onClick = {
                            val intent = Intent(this@MainActivity, FileSaveInternalActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Save File to Internal Storage")
                        }
                        Button(onClick = {

                            val intent = Intent(this@MainActivity, ExternalFileStorageActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Save File to External Storage (MediaStore API)")
                        }

                        Button(onClick = {

                            val intent = Intent(this@MainActivity, SAFActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Save File to External Storage\n(Storage Access Framework API)")
                        }

                    }

                }
            }
        }
    }
}


