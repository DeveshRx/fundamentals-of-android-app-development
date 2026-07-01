package deveshrx.mediaplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import deveshrx.mediaplayer.ui.theme.MediaPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlayerScreen(
                        modifier = Modifier.padding(innerPadding),
                        onPlay = { playAudio() },
                        onStop = {
                            stopAudio()
                        }
                    )
                }
            }
        }
    }

    private fun playAudio() {
        val intent = Intent(this,
            AudioPlayerService::class.java).apply {
            action = AudioPlayerService.ACTION_PLAY
            putExtra(AudioPlayerService.EXTRA_URL,
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
        }
        startService(intent)
    }

    private fun stopAudio() {
        val intent = Intent(this,
            AudioPlayerService::class.java).apply {
            action = AudioPlayerService.ACTION_STOP
        }
        startService(intent)
    }

    @Composable
    fun PlayerScreen(
        modifier: Modifier = Modifier,
        onPlay: () -> Unit,
        onStop: () -> Unit,
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onPlay) {
                Text(text = "Play Audio")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onStop) {
                Text(text = "Stop Audio")
            }
        }
    }

}


