package com.example.musicplayer.screens

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search

enum class AppScreen(
    val icon: ImageVector,
) {
    Home(
        icon = Icons.Filled.Home,
    ),
    Library(
        icon = Icons.Filled.Search,
    ),
    Settings(
        icon = Icons.Filled.Person,
    );

    companion object {
        fun fromRoute(route: String?): AppScreen =
            when (route?.substringBefore("/")) {
                Home.name -> Home
                Library.name -> Library
                Settings.name -> Settings
                null -> Settings
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
