package com.example.musicplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.spotify.PlayingState
import com.example.musicplayer.spotify.SpotifyService

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        setupListeners()
    }

    private fun setupViews () {
        SpotifyService.playingState {
            when(it) {
                PlayingState.PLAYING -> showPauseButton()
                PlayingState.STOPPED -> showPlayButton()
                PlayingState.PAUSED -> showResumeButton()
            }
        }
    }

    private fun showResumeButton() {
        TODO("Not yet implemented")
    }

    private fun showPlayButton() {
        TODO("Not yet implemented")
    }

    private fun showPauseButton() {
        TODO("Not yet implemented")
    }

    private fun setupListeners() {
//        playButton.setOnClickListener {
//            SpotifyService.play("spotify:album:5L8VJO457GXReKVVfRhzyM")
//            showPauseButton()
//        }
//
//        pauseButton.setOnClickListener {
//            SpotifyService.pause()
//            showResumeButton()
//        }
//
//        resumeButton.setOnClickListener {
//            SpotifyService.resume()
//            showPauseButton()
//        }
    }
}