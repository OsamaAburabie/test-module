package expo.modules.testmodule

import android.media.MediaPlayer
import android.net.Uri
import androidx.core.net.toUri
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition



const val AUDIO_PREPARED_EVENT_NAME = "onPrepared"
const val AUDIO_DURATION_EVENT_NAME = "onDuration"

class TestModule : Module() {
  override fun definition() = ModuleDefinition { Name("TestModule")
    val mediaPlayer = MediaPlayer()
    Events(AUDIO_PREPARED_EVENT_NAME, AUDIO_DURATION_EVENT_NAME)
      OnStartObserving {
          mediaPlayer.setOnPreparedListener {
              sendEvent(AUDIO_PREPARED_EVENT_NAME)
          }
      }
        OnStopObserving {
            mediaPlayer.setOnPreparedListener(null)
        }

      Function("hello") {
      "Hello world! 👋"
    }

      AsyncFunction("loadSound") { uri: String ->
          val myUri: Uri = uri.toUri()
          mediaPlayer.apply {
               setDataSource(appContext.reactContext!!.applicationContext, myUri)
              prepare()
              start()
          }

          return@AsyncFunction "Sound loaded"
      }

    Function("pauseSound") {
        mediaPlayer.pause()
    }

    Function("playSound") {
        mediaPlayer.start()
    }

      Function("reset") {
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
