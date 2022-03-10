package com.example.musicplayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NameAndArtist(name: String, artist: String)
{
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h6,
            fontSize = 16.sp
        )
        Text(
            text = artist,
            style = MaterialTheme.typography.caption,
            fontSize = 12.sp,
            textAlign = TextAlign.Left,
        )
    }
}