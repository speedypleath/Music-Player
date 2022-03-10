package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.navigation.NavigationBar
import com.example.musicplayer.navigation.NavigationHost
import com.example.musicplayer.player.Player
import com.example.musicplayer.screens.AppScreen
import com.example.musicplayer.spotify.SpotifyService
import com.example.musicplayer.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
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
                        Player()
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
                Button(onClick = {
                    SpotifyService.connect(this) {
                        val intent = Intent(this, PlayerActivity::class.java)
                        startActivity(intent, )
                    }
                }) {
                    Text("Connect to Spotify")
                }
            }
        }
    }
}
