package com.example.musicplayer.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = blue500,
    primaryVariant = blue300,
    secondary = green300,
    background = black,
//    surface = Black,
//    onPrimary = Black,
//    onSecondary = Black,
//    onBackground = Black,
//    onSurface = Black,
)

//private val LightColorPalette = lightColors(
//    primary = Purple500,
//    primaryVariant = Purple700,
//    secondary = Teal200,
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//)

@Composable
fun MusicPlayerTheme(content: @Composable () -> Unit) {
    val colors = DarkColorPalette
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent
    )
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}