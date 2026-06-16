# How to Send HTTP Requests from Android using OkHttp

This guide walks through integrating OkHttp into an Android (Jetpack Compose) project to make GET and POST requests to a backend server. All code is taken directly from the working project in this repository.

---

## 1. Add the OkHttp Dependency

Open `gradle/libs.versions.toml` and define the OkHttp version:

```toml
[versions]
okhttp = "5.3.2"

[libraries]
okhttp-bom = { group = "com.squareup.okhttp3", name = "okhttp-bom", version.ref = "okhttp" }
okhttp = { group = "com.squareup.okhttp3", name = "okhttp" }
okhttp-logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor" }
```

Then in `app/build.gradle.kts`, add:

```kotlin
dependencies {
    // OkHTTP
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
}
```

> The BOM (Bill of Materials) ensures all OkHttp modules use the same version.

---

## 2. Internet Permission

Add the `INTERNET` permission in `app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<application
    android:usesCleartextTraffic="true"
    ...
>
```

> `usesCleartextTraffic="true"` allows HTTP (non-HTTPS) requests during development. Remove it in production if you only use HTTPS.

---

## 3. Create the OkHttpClient

In your `MainActivity` (or a separate network layer), create a shared client instance:

```kotlin
val client = OkHttpClient()
```

> `OkHttpClient()` with no args gives sensible defaults (timeouts, connection pooling, etc.). You can customise it with a builder if needed.

---

## 4. GET Request

```kotlin
val backend_url = "https://free.mockerapi.com"

fun GET_Request() {
    val request = Request.Builder()
        .url("${backend_url}/get")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (response.isSuccessful) {
                    val responseData = response.body.string()
                    Log.d(TAG, "GET Response: $responseData")
                }
            }
        }
    })
}
```

- `Request.Builder()` constructs an HTTP request.
- `.url(...)` sets the endpoint.
- `.build()` produces the immutable `Request`.
- `client.newCall(request).enqueue(...)` sends the request **asynchronously** on a background thread.
- `response.use { ... }` auto-closes the response body.
- `response.body.string()` returns the raw JSON string.

---

## 5. POST Request with JSON Body

```kotlin
fun POST_Request() {
    val json = """
        {
            "used_id": "myUser12345"
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
                    Log.d(TAG, "POST Response: $responseData")
                }
            }
        }
    })
}
```

Key differences from GET:
- `json.toRequestBody(mediaType)` converts a Kotlin string into an OkHttp `RequestBody` with the correct MIME type.
- `.addHeader("Content-Type", "application/json")` explicitly sets the content-type header.
- `.post(body)` sets the HTTP method and body.

---

## 6. Triggering from a Button (Compose UI)

```kotlin
Button(onClick = { GET_Request() }) {
    Text("Send GET Request")
}
Button(onClick = { POST_Request() }) {
    Text("Send POST Request")
}
```

---

## 7. Important Notes

- **Async by default**: `enqueue` runs the request on OkHttp's internal thread pool and delivers the response on a background thread. **Do not** update the UI directly inside `onResponse` — use a `Handler`, `LiveData`, or Compose `State` to post results back to the main thread.
- **Error handling**: `onFailure` is called when the request could not be executed (network error, timeout, etc.). Non-2xx responses still arrive in `onResponse` — you must check `response.isSuccessful`.
- **Response body**: Call `response.body.string()` only once. The body is consumed after reading and cannot be re-read.
- **Logging**: Add the logging interceptor to debug requests/responses:

```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()
```

---

