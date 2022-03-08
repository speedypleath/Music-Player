package com.example.musicplayer.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.ui.theme.white

@Composable
fun Player() {
    var progress by remember { mutableStateOf(0.1f) }
    val increaseProgress = {
        progress += 0.1f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    BottomAppBar(
        modifier = Modifier
            .height(78.dp)
            .fillMaxWidth()
            .absolutePadding(left = 16.dp, right = 16.dp)
            .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Gray,
                        Color.Black,
                    ),
                ),
                alpha = 0.2f
            ),
        backgroundColor = Color.Transparent,
        contentColor = white,
        elevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
                    .wrapContentHeight(Alignment.CenterVertically, true),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NameAndArtist(name = "Name of the song", artist = "Name of the artist")
                ActionIcons(increaseProgress)
            }
            Spacer(modifier = Modifier.height(IntrinsicSize.Max))
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxSize(1f)
                    .wrapContentHeight(Alignment.Bottom, true),

                backgroundColor = Color.Gray,
                color = Color.Green,
            )
        }
    }
}

@Composable
fun NameAndArtist(name: String, artist: String)
{
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h6,
            fontSize = 16.sp
        )
        Text(
            text = artist,
            style = MaterialTheme.typography.caption,
            fontSize = 12.sp,
            textAlign = TextAlign.Left,
        )
    }
}

@Composable
fun ActionIcons(increaseProgress: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxSize(1f)
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            "Play/Pause",
            modifier = Modifier
                .padding(end = 12.dp)
                .size(32.dp)
                .fillMaxSize(1f)
                .scale(1.2f)
                .clickable { increaseProgress() }
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            "Repeat",
            modifier = Modifier
                .padding(end = 14.dp)
                .size(32.dp)
                .fillMaxSize(1f)
        )
    }
}
