package com.example.musicplayer.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    windowHeight: Int,
    content: @Composable () -> Unit
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        var y = windowHeight - 2 * placeables[1].height

        layout(
            height = constraints.maxHeight,
            width = constraints.maxWidth
        ) {
            placeables.reversed().forEach { placeable ->
                y -= placeable.height
                placeable.placeRelative(x = 0, y = y)
            }
        }
    }
}