package com.example.musicplayer.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.musicplayer.data.SettingsViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun OpenFolder() {
    val settingsViewModel = getViewModel<SettingsViewModel>()
    Button(
        onClick = settingsViewModel.action,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Open Folder")
    }
}

@Composable
fun SettingsBody() {
    OpenFolder()
}