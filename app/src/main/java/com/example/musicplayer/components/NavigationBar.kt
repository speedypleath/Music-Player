package com.example.musicplayer.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.musicplayer.screens.AppScreen
import com.example.musicplayer.ui.theme.white

@Composable
fun NavigationBar(
    allScreens: List<AppScreen>,
    onTabSelected: (AppScreen) -> Unit,
    currentScreen: AppScreen
) {
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .absolutePadding(left = 16.dp, right = 16.dp)
            .clip(RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.DarkGray
                    ),
                    startY = 90f
                ),
                alpha = 0.9f
            ),
        contentColor = white,
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
    )
    {
        allScreens.forEach { screen ->
            BottomNavigationItem(
                label = { Text(screen.name) },
                icon = { Icon(imageVector = screen.icon, contentDescription = "") },
                onClick = { onTabSelected(screen) },
                selected = currentScreen == screen
            )
        }
    }
}
