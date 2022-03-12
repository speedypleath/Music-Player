package com.example.musicplayer.spotify

import android.content.Context
import android.util.Log
import com.example.musicplayer.BuildConfig
import com.example.musicplayer.data.Song
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.Track
import javax.security.auth.callback.Callback

enum class PlayingState {
    PAUSED, PLAYING, STOPPED
}


object SpotifyService {
    private const val CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID
    private const val REDIRECT_URI = BuildConfig.SPOTIFY_REDIRECT_URI
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var connectionParams: ConnectionParams = ConnectionParams.Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()

    fun connect(context: Context) {
        if (spotifyAppRemote?.isConnected == true) {
            return
        }
        val connectionListener = object : Connector.ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                this@SpotifyService.spotifyAppRemote = spotifyAppRemote
                Log.d("SpotifyService", "Connected!")
            }
            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifyService", throwable.message, throwable)
            }
        }
        SpotifyAppRemote.connect(context, connectionParams, connectionListener)
    }

    fun play(uri: String): CallResult<Empty>? {
        return spotifyAppRemote?.playerApi?.play(uri)
    }

    fun resume() {
        spotifyAppRemote?.playerApi?.resume()
    }

    fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }

    fun playingState(handler: (PlayingState) -> Unit) {
        spotifyAppRemote?.playerApi?.playerState?.setResultCallback { result ->
            when {
                result.track.uri == null -> {
                    handler(PlayingState.STOPPED)
                }
                result.isPaused -> {
                    handler(PlayingState.PAUSED)
                }
                else -> {
                    handler(PlayingState.PLAYING)
                }
            }
        }
    }

    fun disconnect() {
        Log.d("SpotifyService", "Disconnected!")
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    fun subscribeToTrackPosition(handler: (Long) -> Unit) {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback { result ->
            handler(result.playbackPosition)
        }
    }

    fun subscribeToTrack(handler: (Track) -> Unit) {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            handler(it.track)
        }
    }
}
