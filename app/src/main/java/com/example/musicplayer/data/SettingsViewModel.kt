package com.example.musicplayer.data

import androidx.lifecycle.ViewModel
import kotlin.reflect.KProperty

typealias Click = () -> Unit
class SettingsViewModel (
    val action: Click
) : ViewModel() {
    operator fun getValue(songListViewModel: SongListViewModel, property: KProperty<*>): Any {
        return action
    }
}

