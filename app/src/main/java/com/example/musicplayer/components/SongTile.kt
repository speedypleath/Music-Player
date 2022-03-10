package com.example.musicplayer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.musicplayer.player.ActionIcons

@Composable
fun SongTile() {
    Row(
        modifier = Modifier.fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically, true),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NameAndArtist(name = "Name of the song", artist = "Name of the artist")
    }
}