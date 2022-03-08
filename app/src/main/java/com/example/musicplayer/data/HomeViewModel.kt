package com.example.musicplayer.data

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
data class HomeViewModel @Inject constructor (
    val string: String
) : ViewModel()

