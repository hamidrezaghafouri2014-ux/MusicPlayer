package com.example.musicplayer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicplayer.viewmodel.MusicViewmodel

@Composable
fun Play(viewModel: MusicViewmodel) {
    val currentSong by viewModel.currentSong.collectAsState()

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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = song.artist,
                style = MaterialTheme.typography.titleMedium,
                color = Color.LightGray,
                maxLines = 1
            )

        } ?: run {
            Text(
                text = "No song playing",
                color = Color.White
            )
        }
    }
}
