package com.example.musicplayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicplayer.data.SongListViewModel

@Composable
fun SongList(
    modifier: Modifier = Modifier,
    viewModel: SongListViewModel = viewModel(),
) {
    val musicCards = viewModel.musicCards
    val context = LocalContext.current
    viewModel.initializeListIfNeeded(context = context)
    LazyColumn {
        modifier.fillMaxSize()
        itemsIndexed(musicCards) { index, card ->
            LaunchedEffect(Unit) {
                viewModel.loadBitmapIfNeeded(context, index)
            }
            if (viewModel.isBitmapLoaded(index)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(
                        horizontal = 4.dp,
                        vertical = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = card.image!!.asImageBitmap(), "",
                        modifier = Modifier.size(
                            52.dp,
                            52.dp
                        )
                    )
                    NameAndArtist(name = card.artist, artist = card.title)
                }
            }
        }
    }
}