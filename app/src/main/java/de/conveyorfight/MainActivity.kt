package de.conveyorfight

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    }

    // save current track do disable changing to same track
    private var currentTrack: Int? = null

    /**
     * build and start a new player with the chosen track
     */
    fun changeTrack(trackId: Int) {
        // check if track needs to be changed
        if (currentTrack != trackId) {
            // reset player with new track
            mediaPlayer?.stop()
            mediaPlayer = MediaPlayer.create(this, trackId)
            mediaPlayer?.isLooping = true

            // initialize player
            setVolume()
            mediaPlayer?.start()
            currentTrack = trackId
        }
    }

    /**
     * adjust both volume settings to current value in application
     */
    fun setVolume() {
        val application: ConveyorApplication =
            (application) as ConveyorApplication
        mediaPlayer?.setVolume(
            application.volumeMusic.toFloat() / application.volumeEffectMax,
            application.volumeMusic.toFloat() / application.volumeEffectMax
        )
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // hide ui components when app is focused
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // implementation for current versions
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                // hide components
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                // activate swipe gesture
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            //legacy implementation
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }


}