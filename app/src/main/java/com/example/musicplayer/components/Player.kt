package com.example.musicplayer.components

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
import androidx.compose.ui.unit.dp
import com.example.musicplayer.data.SongViewModel
import com.example.musicplayer.ui.theme.white

@Composable
fun Player(songViewModel: SongViewModel = remember { SongViewModel() }) {
    val progress by remember { mutableStateOf(0.1f) }
    var name by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }

    songViewModel.initSong()
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
                        Color.DarkGray.copy(alpha = 0.8f),
                        Color.Black,
                    ),
                    endY = 60f,
                ),
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
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(Alignment.CenterVertically, true),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NameAndArtist(name = name, artist = artist)
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
                            .clickable {
                                songViewModel.play("spotify:album:5L8VJO457GXReKVVfRhzyM")
                                name = songViewModel.getName
                                artist = songViewModel.getArtist
                            }
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
