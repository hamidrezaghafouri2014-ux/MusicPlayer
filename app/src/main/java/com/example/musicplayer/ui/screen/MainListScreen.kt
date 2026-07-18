package com.example.musicplayer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.musicplayer.model.Song

@Composable
fun MainList(songs: List<Song>, player: ExoPlayer, onSongClick: (Song) -> Unit) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        items(songs) { song ->

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        player.setMediaItem(MediaItem.fromUri(song.uri))
                        player.prepare()
                        player.play()
                        onSongClick(song)
                    }
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    AsyncImage(
                        model = song.artworkUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(Modifier.width(16.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            maxLines = 1
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = song.artist,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            maxLines = 1
                        )
                        Spacer(Modifier.height(20.dp))

                    }
                }
            }
        }
    }
}