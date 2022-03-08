package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.navigator.NavigationBar
import com.example.musicplayer.navigator.NavigationHost
import com.example.musicplayer.player.Player
import com.example.musicplayer.screens.AppScreen
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
            NavigationHost(navController, modifier = Modifier.padding(innerPadding))
        }
    }
}
}

