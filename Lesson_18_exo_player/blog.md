# Streaming Video in Android with Media3 ExoPlayer & Jetpack Compose

This guide walks you through building a modern video player in Android using the **Media3 ExoPlayer** library and **Jetpack Compose**. We'll cover everything from setup to playing both network streams and local files.

---

## 1. Introduction to Media3 ExoPlayer
**Media3** is the latest media library from Google, succeeding the original ExoPlayer and MediaCompat libraries. It simplifies the integration between the playback engine and the UI.

### Core Concepts:
*   **ExoPlayer**: The playback engine. It handles media loading, buffering, and decoding.
*   **PlayerView**: The UI component that renders the video and provides playback controls (Play/Pause, Seekbar).
*   **MediaItem**: A representation of a piece of media (URL or local file) and its metadata.

---

## 2. Project Setup

### Dependencies
Add the following to your `build.gradle.kts` (Module: app) file. In this project, we use version `1.5.1`.

```kotlin
dependencies {
    // Core ExoPlayer functionality
    implementation("androidx.media3:media3-exoplayer:1.5.1")
    // UI components like PlayerView
    implementation("androidx.media3:media3-ui:1.5.1")
    // Shared media constants and classes
    implementation("androidx.media3:media3-common:1.5.1")
}
```

### Manifest Configuration
To stream video from the internet, you need the `INTERNET` permission. If you're playing local files, ensure you have the appropriate storage permissions.

```xml
<!-- AndroidManifest.xml -->
<manifest ...>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />

    <application
        ...
        android:usesCleartextTraffic="true"> <!-- Needed for http:// (non-https) streams -->
        ...
    </application>
</manifest>
```

---

## 3. Implementation

### Initializing the Player
The player should be tied to the Activity's lifecycle. We initialize it in `onCreate` and **must** release it in `onDestroy` to free up hardware decoders.

```kotlin
class MainActivity : ComponentActivity() {
    lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Initialize the player
        player = ExoPlayer.Builder(this).build()

        setContent {
            // UI Code here
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 2. Always release the player to prevent memory leaks
        player.release()
    }
}
```

### The Video Player Surface (Compose Bridge)
Since `PlayerView` is a traditional Android `View`, we use `AndroidView` to host it inside our Compose UI.

```kotlin
@Composable
fun VideoPlayerSurface(
    player: Player?,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
                // Configure UI settings here
                this.useController = true 
            }
        },
        update = { view ->
            // Keep the view in sync with the player state
            view.player = player
        },
        modifier = modifier
    )
}
```

### Playing a Network Stream
To play a video from a URL, create a `MediaItem`, set it, and call `prepare()`.

```kotlin
fun playFromUrl(url: String) {
    val mediaItem = MediaItem.fromUri(url)
    player.setMediaItem(mediaItem)
    player.prepare() // Player starts buffering
    player.play()    // Player starts visuals
}
```

### Playing a Local File
Using the modern `ActivityResultContracts`, we can let users pick a video from their gallery.

```kotlin
val filePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    uri?.let {
        player.setMediaItem(MediaItem.fromUri(it))
        player.prepare()
        player.play()
    }
}

// Trigger in UI
Button(onClick = { filePickerLauncher.launch("video/*") }) {
    Text("Select Local Video")
}
```

---

## 4. Key Takeaways
1.  **Lifecycle Management**: Always call `player.release()` in `onDestroy`.
2.  **Cleartext Traffic**: Enable `android:usesCleartextTraffic="true"` in the manifest if your video URLs are not `https`.
3.  **Aspect Ratio**: Use `Modifier.aspectRatio(16f / 9f)` on your `VideoPlayerSurface` to ensure a consistent video box.
4.  **Prepare then Play**: Always call `player.prepare()` before `player.play()` to ensure the engine starts loading the stream.

---
*Generated based on the code in this project.*
