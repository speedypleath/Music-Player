package com.example.musicplayer.data

import android.graphics.Bitmap
import android.net.Uri

data class SongModel(
    val title: String,
    val artist: String,
    val album: String?,
    val duration: Long,
    val year: Int?,
    val uri: Uri?,
    val image: Bitmap?,
)