package com.example.musicplayer.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.spotify.SpotifyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class SongState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val isShuffle: Boolean = false,
    val isRepeat: Boolean = false,
)

class SongViewModel : ViewModel() {
    private lateinit var _song: MutableLiveData<SongModel>
    private val _songState = MutableStateFlow(SongState())

//    fun timeElapsed(): Float {
//        return _songState.value.currentPosition / _song.value?.duration!!.toFloat()
//    }
//
//    fun changeSong(song: Song) {
//        _song.value = song
//    }

    val getName: String
        get() = _song.value?.title!!

    val getArtist: String
        get() = _song.value?.artist!!

    fun initSong() {
        _song = MutableLiveData()
        _song.value = SongModel("Title of song", "Artist of song")
    }

    fun play(uri: String) {
        viewModelScope.launch {
            val request = SpotifyService.play(uri)
            request?.setResultCallback {
                Log.d("SongViewModel", "Played")


                SpotifyService.subscribeToTrackPosition {
                    _songState.value = _songState.value.copy(currentPosition = it)
                    Log.d("SongViewModel", "position: $it")
                }
                SpotifyService.subscribeToTrack { song ->
                    _song.value = SongModel(
                        song.name,
                        song.artists[0].name,
                        song.album.name,
                        song.duration,
//                        song.uri,
//                        song.imageUri.toString()
                    )
                    Log.d("SongViewModel", "song: ${_song.value.toString()}")
                }
                _songState.value = _songState.value.copy(isPlaying = true)
            }
        }
    }

//    private fun pause() {
//        _songState.value = _songState.value.copy(isPlaying = false)
//    }
//
//    private fun resume() {
//        SpotifyService.resume()
//        _songState.value = _songState.value.copy(isPlaying = true)
//    }

//    fun isPlaying(): Boolean {
//        return _songState.value.isPlaying
//    }
//
//    fun togglePlayPause() {
//        if (_songState.value.isPlaying) {
//            pause()
//        } else {
//            resume()
//        }
//    }
//
//    fun seekTo(position: Float) {
//        _songState.value = _songState.value.copy(currentPosition = (position * _song.value?.duration!!).toLong())
//    }
//
//    fun toggleShuffle() {
//        _songState.value = _songState.value.copy(isShuffle = !_songState.value.isShuffle)
//    }
//
//    fun toggleRepeat() {
//        _songState.value = _songState.value.copy(isRepeat = !_songState.value.isRepeat)
//    }

}


