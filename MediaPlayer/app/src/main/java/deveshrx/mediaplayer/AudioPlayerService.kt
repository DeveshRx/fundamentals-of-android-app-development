package deveshrx.mediaplayer

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class AudioPlayerService : Service(),
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action.equals(ACTION_PLAY)){
            intent?.getStringExtra(EXTRA_URL)?.let { url ->
                playAudio(url)
            }
        }

        if (intent?.action.equals(ACTION_STOP)){
            stopAudio()
        }


        return START_STICKY
    }

    private fun playAudio(url: String) {
        stopAudio()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build(),
            )
            setDataSource(url)
            setOnPreparedListener(this@AudioPlayerService)
            setOnErrorListener(this@AudioPlayerService)
            prepareAsync() // prepare async to not block main thread
        }
    }

    private fun playAudioFromRaw() {
        stopAudio()

        val audioFile = resources.openRawResourceFd(R.raw.music) ?: return // AssetFileDescriptor

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build(),
            )
            // Use the FileDescriptor from the raw resource
            setDataSource(audioFile.fileDescriptor, audioFile.startOffset, audioFile.length)
            audioFile.close()

            setOnPreparedListener(this@AudioPlayerService)
            setOnErrorListener(this@AudioPlayerService)
            prepareAsync() // prepare async to not block main thread
        }
    }

    private fun stopAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                Toast.makeText(this, "Audio Stopped...", Toast.LENGTH_SHORT).show()
            }
            it.release()
        }
        mediaPlayer = null
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
        Toast.makeText(this, "Playing Audio...", Toast.LENGTH_SHORT).show()

    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e("AudioPlayerService", "MediaPlayer error: what=$what, extra=$extra")
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_PLAY = "deveshrx.mediaplayer.action.PLAY"
        const val ACTION_STOP = "deveshrx.mediaplayer.action.STOP"
        const val EXTRA_URL = "deveshrx.mediaplayer.extra.URL"
    }
}
