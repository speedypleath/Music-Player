package com.example.musicplayer.data

import androidx.lifecycle.ViewModel
import javax.inject.Inject

data class SettingsViewModel @Inject constructor (
    val string: String
) : ViewModel()