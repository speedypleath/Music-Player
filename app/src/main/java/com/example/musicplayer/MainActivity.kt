package com.example.musicplayer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT_TREE
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import com.example.musicplayer.components.BottomBar
import com.example.musicplayer.components.Player
import com.example.musicplayer.data.SettingsViewModel
import com.example.musicplayer.navigation.NavigationBar
import com.example.musicplayer.navigation.NavigationHost
import com.example.musicplayer.screens.AppScreen
import com.example.musicplayer.spotify.SpotifyService
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class MainActivity : ComponentActivity() {
    private val openFolder =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: return@registerForActivityResult
                val contentResolver = applicationContext.contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.outgoingPersistedUriPermissions
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                Log.d("MainActivity", "Selected directory: $uri")
            } else {
                Log.d("MainActivity", "No directory selected")
            }
        }

    private val windowMetrics by lazy {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    }
    private val actionIntent = Intent(ACTION_OPEN_DOCUMENT_TREE)
    private val action = {
        openFolder.launch(actionIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }

        super.onCreate(savedInstanceState)
        setContent {
            WindowHeight(windowMetrics = windowMetrics) { height ->
                App(height = height)
            }


        }
    }

    override fun onStart() {
        getViewModel<SettingsViewModel> { parametersOf(action) }
        super.onStart()
        SpotifyService.connect(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SpotifyService.disconnect()
    }
}

@Composable
fun WindowHeight(windowMetrics:  WindowMetrics, children: @Composable (Int) -> Unit) {
    val windowHeight = remember {
        windowMetrics.bounds.bottom
    }
    children(windowHeight)
}

@Composable
fun App(height: Int) {
    val allScreens = AppScreen.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.fromRoute(backstackEntry.value?.destination?.route)


    MusicPlayerTheme {
        Scaffold { innerPadding ->
            NavigationHost(navController, modifier = Modifier.padding(innerPadding))
            BottomBar(windowHeight = height) {
                Player()
                NavigationBar(
                    allScreens = allScreens,
                    onTabSelected = { screen ->
                        navController.navigate(screen.name)
                    },
                    currentScreen = currentScreen,
                )
            }
        }
    }
}




