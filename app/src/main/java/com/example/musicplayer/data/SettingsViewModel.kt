package com.example.musicplayer.data

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.truth.content.IntentCorrespondences.data
import com.google.ads.interactivemedia.v3.internal.it
import kotlin.reflect.KProperty

typealias Click = () -> Unit
class SettingsViewModel (
    val action: Click
) : ViewModel() {
    operator fun getValue(songListViewModel: SongListViewModel, property: KProperty<*>): Any {
        return action
    }
}

