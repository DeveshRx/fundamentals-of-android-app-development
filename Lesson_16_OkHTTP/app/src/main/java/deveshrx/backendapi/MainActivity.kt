package deveshrx.backendapi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import deveshrx.backendapi.ui.theme.BackendAPITheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class MainActivity : ComponentActivity() {
    val TAG="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BackendAPITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Button(onClick = {GET_Request()}) {
                            Text("Send GET Request")
                        }
                        Button(onClick = {POST_Request()}) {
                            Text("Send POST Request")
                        }
                    }
                }
            }
        }
    }

    val backend_url="https://free.mockerapi.com"
    val client = OkHttpClient()
    fun GET_Request(){

        val request = Request.Builder()
            .url("${backend_url}/get")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val responseData = response.body.string()
                        Log.d(TAG, "onResponse: GET Response: ${responseData}")
                    }
                }
            }
        })
    }

    fun POST_Request(){

        val json = """
            { 
            "used_id":"myUser12345"
            }
            """.trimIndent()

        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("${backend_url}/post")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val responseData = response.body.string()
                        Log.d(TAG, "onResponse: POST Response: ${responseData}")
                    }
                }
            }
        })
    }


}

