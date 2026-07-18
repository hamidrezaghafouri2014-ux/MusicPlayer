package com.example.musicplayer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.musicplayer.viewmodel.MusicViewmodel

@Composable
fun Play(viewModel: MusicViewmodel, player: ExoPlayer) {
    val currentSong by viewModel.currentSong.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    LaunchedEffect(player) {
        viewModel.startProgressUpdate(player)
    }

    LaunchedEffect(currentSong) {
        currentSong?.let { song ->
            if (player.currentMediaItem?.mediaId != song.id.toString()) {
                player.setMediaItem(MediaItem.Builder().setMediaId(song.id.toString()).setUri(song.uri).build())
                player.prepare()
                player.play()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        currentSong?.let { song ->
            AsyncImage(
                model = song.artworkUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = song.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                maxLines = 1
            )

            Text(
                text = song.artist,
                style = MaterialTheme.typography.titleMedium,
                color = Color.LightGray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Timeline / Slider
            Slider(
                value = currentPosition.toFloat(),
                onValueChange = { player.seekTo(it.toLong()) },
                valueRange = 0f..duration.toFloat().coerceAtLeast(1f),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Gray
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatTime(currentPosition), color = Color.White)
                Text(text = formatTime(duration), color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                IconButton(onClick = { 
                    viewModel.previousSong()
                }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                }

                IconButton(
                    onClick = {
                        if (isPlaying) player.pause() else player.play()
                    },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }

                IconButton(onClick = { 
                    viewModel.nextSong()
                }) {
                    Icon(Icons.Default.SkipNext, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                }
            }
        } ?: run {
            Text(
                text = "No song playing",
                color = Color.White
            )
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
