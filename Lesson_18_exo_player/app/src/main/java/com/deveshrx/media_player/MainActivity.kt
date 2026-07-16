package com.deveshrx.media_player

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.deveshrx.media_player.ui.theme.Media_playerTheme

class MainActivity : ComponentActivity() {

    lateinit var player: ExoPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        player = ExoPlayer.Builder(this).build()


        setContent {
            Media_playerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MainScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val scrollState = rememberScrollState()
        val context = LocalContext.current

        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                player.setMediaItem(MediaItem.fromUri(it))
                // player will start loading media
                player.prepare()
                player.play()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            VideoPlayerSurface(
                player = player,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            Button(onClick = {
                playVideo()
            }) { Text("Play from URL") }

            Button(onClick = {
                filePickerLauncher.launch("video/*")
            }) { Text("Play from File") }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    val video_url =
        "https://github.com/chthomos/video-media-samples/raw/refs/heads/master/big-buck-bunny-1080p-30sec.mp4"

    fun playVideo() {
        val mediaItem = MediaItem.fromUri(video_url)

        player.setMediaItem(mediaItem)
        player.prepare() // player will start loading media
        player.play()

        /*player.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }*/
    }

    @Composable
    fun VideoPlayerSurface(
        player: Player?,
        modifier: Modifier = Modifier
    ) {
        AndroidView(
            factory = { context ->
                // Create the PlayerView once
                PlayerView(context).apply {
                    this.player = player
                }
            },
            update = { view ->
                // Update the player whenever it changes
                view.player = player
            },
            modifier = modifier
        )
    }

}

