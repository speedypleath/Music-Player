package com.example.musicplayer

import android.Manifest
import android.R.string
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT_TREE
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile.fromTreeUri
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.components.Player
import com.example.musicplayer.data.SettingsViewModel
import com.example.musicplayer.data.SongViewModel
import com.example.musicplayer.navigation.NavigationBar
import com.example.musicplayer.navigation.NavigationHost
import com.example.musicplayer.screens.AppScreen
import com.example.musicplayer.spotify.SpotifyService
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.Permission


class MainActivity : ComponentActivity() {
    private val openFolder = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@registerForActivityResult
            val contentResolver = applicationContext.contentResolver
            val documentFile = fromTreeUri(applicationContext, uri)
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            applicationContext.getSharedPreferences("settings", 0).edit().putString("path", uri.toString()).apply()

            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.outgoingPersistedUriPermissions
            contentResolver.takePersistableUriPermission(uri, takeFlags)
            Log.d("MainActivity", "Selected directory: $uri")
            // ...
        } else {
            // The user has not selected a directory.
            // Maybe they pressed Back or otherwise cancelled the action?
            Log.d("MainActivity", "No directory selected")
            // ...
        }
    }
    private val actionIntent = Intent(ACTION_OPEN_DOCUMENT_TREE)
    private val action = {
        openFolder.launch(actionIntent)
    }

    private val songViewModel: SongViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        if (applicationContext.getSharedPreferences("settings", 0).getString("path", null) == null) {
            action()
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }

        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    override fun onStart() {
        getViewModel<SettingsViewModel>{ parametersOf(action) }

        super.onStart()
        SpotifyService.connect(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SpotifyService.disconnect()
    }
    @Composable
    fun App() {
        val allScreens = AppScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = AppScreen.fromRoute(backstackEntry.value?.destination?.route)
        MusicPlayerTheme {
            Scaffold(
                bottomBar = {
                    Column {
                        Player(songViewModel)
                        NavigationBar(
                            allScreens = allScreens,
                            onTabSelected = { screen ->
                                navController.navigate(screen.name)
                            },
                            currentScreen = currentScreen
                        )
                    }
                },
            ) { innerPadding ->
                NavigationHost(navController, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

