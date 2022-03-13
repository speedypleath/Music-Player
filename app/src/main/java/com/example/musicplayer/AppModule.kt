package com.example.musicplayer


import com.example.musicplayer.data.SettingsViewModel
import com.example.musicplayer.data.SongViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { parameters -> SettingsViewModel(action = parameters.get()) }
    viewModel { SongViewModel() }
}

val testModule = module {
    single {SettingsViewModel(action = {}) }
    viewModel { SongViewModel() }
}