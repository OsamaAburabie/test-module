package expo.modules.testmodule

import android.media.MediaPlayer
import android.net.Uri
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import java.util.*


const val AUDIO_PREPARED_EVENT_NAME = "onPrepared"
const val AUDIO_DURATION_EVENT_NAME = "onDuration"
const val AUDIO_POSITION_EVENT_NAME = "onPosition"
const val CUSTOM_EVENT_NAME = "onCustomEvent"
const val AUDIO_STATE_CHANGE = "onStateChange"
const val AUDIO_IS_PLAYING_CHANGE = "onIsPlayingChange"

class MediaPlayerEventListener(
    private val mediaPlayer: MediaPlayer,
    private val onPositionChanged: ((Int) -> Unit)? = null
) {
    private var timer: Timer? = null

    init {
        startTimer()
    }

    fun cancel() {
        stopTimer()
    }

    fun pause() {
        pauseTimer()
    }

    fun resume() {
        resumeTimer()
    }

    private fun startTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                onPositionChanged?.invoke(mediaPlayer.currentPosition)
            }
        }, 0, 100) // 100 ms
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    private fun pauseTimer() {
        timer?.cancel()
    }

    private fun resumeTimer() {
        startTimer()
    }
}

class TestModule : Module() {
    private var mPlayer: ExoPlayer? = null

    override fun definition() = ModuleDefinition {
        Name("TestModule")

        Events(
            AUDIO_PREPARED_EVENT_NAME,
            AUDIO_DURATION_EVENT_NAME,
            AUDIO_POSITION_EVENT_NAME,
            CUSTOM_EVENT_NAME,
            AUDIO_STATE_CHANGE,
            AUDIO_IS_PLAYING_CHANGE
        )

        OnStartObserving {
            mPlayer?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    sendEvent(AUDIO_STATE_CHANGE, mapOf("state" to state))
                }
            })

            mPlayer?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    sendEvent(AUDIO_IS_PLAYING_CHANGE, mapOf("isPlaying" to isPlaying))
                }
            })
        }

        OnStopObserving {
            mPlayer?.removeListener(object : Player.Listener {})
        }

        Function("init") {
            // Create a new instance of the player if it's not created yet
            if (mPlayer == null) {
                mPlayer = ExoPlayer.Builder(appContext.reactContext!!.applicationContext).build()
            }
        }

        Function("loadSound") { uri: String ->
            // Create a media item from the URI
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .build()
            mPlayer?.setMediaItem(mediaItem)

            // Prepare the player
            mPlayer?.prepare()

            mPlayer?.play()
        }


        Function("pauseSound") {
            mPlayer?.pause()
        }

        Function("playSound") {
            mPlayer?.play()
        }

        Function("reset") {
            mPlayer?.release()
        }

        Function("stop") {
            mPlayer?.stop()
        }

        Function("getDuration") {
           return@Function mPlayer?.duration
        }
        Function("getPosition") {
           return@Function mPlayer?.currentPosition
        }

    }
}

