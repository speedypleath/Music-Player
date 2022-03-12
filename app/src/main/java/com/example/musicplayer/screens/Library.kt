package com.example.musicplayer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LibraryBody() {
    Row(
        modifier = Modifier.fillMaxSize().background(color = Color.Red)
    ) {
        Text(text = "Library")
    }

}