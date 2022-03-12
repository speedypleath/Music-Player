package com.example.musicplayer.data

import android.graphics.Bitmap
import android.net.Uri

data class SongModel(
    val title: String,
    val artist: String,
    val album: String? = null,
    val duration: Long = 0,
    val year: Int? = null,
    val uri: Uri? = null,
    val image: Bitmap? = null
)