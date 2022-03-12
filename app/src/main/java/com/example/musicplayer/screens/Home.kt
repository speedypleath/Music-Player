package com.example.musicplayer.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicplayer.data.SettingsViewModel
import com.example.musicplayer.data.SongListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SongList(
    viewModel: SongListViewModel = viewModel(),
) {
    val musicCards = viewModel.musicCards
    val context = LocalContext.current
    viewModel.initializeListIfNeeded(context = context)
    LazyColumn {
        itemsIndexed(musicCards) { index, card ->
            LaunchedEffect(Unit) {
                viewModel.loadBitmapIfNeeded(context, index)
            }
            if (card.image != null) {
                Image(
                    bitmap = card.image.asImageBitmap(), "",
                    modifier = Modifier.size(
                        100.dp,
                        100.dp
                    )
                )
            } else {
                Text(text = card.title)
            }
        }
    }
}

@Composable
fun HomeBody(function: () -> Unit) {
    SongList()
}