package expo.modules.testmodule

import android.media.MediaPlayer
import android.net.Uri
import androidx.core.net.toUri
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import java.util.Timer
import java.util.TimerTask


const val AUDIO_PREPARED_EVENT_NAME = "onPrepared"
const val AUDIO_DURATION_EVENT_NAME = "onDuration"
const val AUDIO_POSITION_EVENT_NAME = "onPosition"
const val CUSTOM_EVENT_NAME = "onCustomEvent"

class MediaPlayerEventListener(private val mediaPlayer: MediaPlayer, private val onPositionChanged: ((Int) -> Unit)? = null) {
    private var timer: Timer? = null

    init {
        startTimer()
    }

    fun cancel() {
        stopTimer()
    }

    fun pause() {
        pauseTimer()
    }   fun resume() {
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
    private val mediaPlayer = MediaPlayer()
    private var mediaPlayerEventListener: MediaPlayerEventListener? = null

    override fun definition() = ModuleDefinition {
        Name("TestModule")

        Events(AUDIO_PREPARED_EVENT_NAME, AUDIO_DURATION_EVENT_NAME, AUDIO_POSITION_EVENT_NAME, CUSTOM_EVENT_NAME)

        OnStartObserving {
            mediaPlayer.setOnPreparedListener {
                sendEvent(AUDIO_PREPARED_EVENT_NAME)
                }

            mediaPlayerEventListener = MediaPlayerEventListener(mediaPlayer) { position ->
                // Send an event when the position changes
                println("Position changed: $position")
                sendEvent(AUDIO_POSITION_EVENT_NAME, mapOf("position" to position))
            }
        }

        OnStopObserving {
            mediaPlayer.setOnPreparedListener(null)
            mediaPlayerEventListener?.cancel()
            mediaPlayerEventListener = null
        }

        AsyncFunction("loadSound") { uri: String ->
            val myUri: Uri = uri.toUri()
            mediaPlayer.apply {
                setDataSource(appContext.reactContext!!.applicationContext, myUri)
                prepare()
                start()

                // Send an event when the duration changes
                sendEvent(AUDIO_DURATION_EVENT_NAME, mapOf("duration" to duration))
                sendEvent(CUSTOM_EVENT_NAME, mapOf("custom" to "playing"))
            }

            return@AsyncFunction "Sound loaded"
        }

        Function("pauseSound") {
            sendEvent(CUSTOM_EVENT_NAME, mapOf("custom" to "paused"))
            mediaPlayer.pause()
            mediaPlayerEventListener?.pause()
        }

        Function("playSound") {
            sendEvent(CUSTOM_EVENT_NAME, mapOf("custom" to "playing"))
            mediaPlayer.start()
            mediaPlayerEventListener?.resume()

            // Create a new instance of the custom event listener and pass in the media player
        }

        Function("reset") {
            sendEvent(CUSTOM_EVENT_NAME, mapOf("custom" to "reset"))
            sendEvent(AUDIO_DURATION_EVENT_NAME, mapOf("id" to 0))
            mediaPlayer.reset()
        }

        Function("getDuration") {
            return@Function mediaPlayer.duration
        }

        Function("getCurrentPosition") {
            return@Function mediaPlayer.currentPosition
        }

    }
}

