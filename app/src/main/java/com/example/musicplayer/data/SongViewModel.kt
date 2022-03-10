package com.example.musicplayer.data

import androidx.lifecycle.ViewModel

data class SongViewModel constructor(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
    val path: String
) : ViewModel()
