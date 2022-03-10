package com.example.musicplayer.screens

import android.content.Intent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat.startActivity
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.spotify.SpotifyService

@Composable
fun HomeBody(function: () -> Unit) {
    Text(text = "Home Screen")
}