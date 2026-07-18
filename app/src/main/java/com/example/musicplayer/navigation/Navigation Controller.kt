package com.example.musicplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.ui.screen.MainList
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.model.Song
import com.example.musicplayer.viewmodel.MusicViewmodel
import com.example.musicplayer.ui.screen.Play
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object PlayRoute

@Composable
fun NavigationC(songs: List<Song>, player: ExoPlayer, viewModel: MusicViewmodel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            MainList(
                songs = songs,
                player = player,
                onSongClick = { song ->
                    viewModel.setCurrentSong(song)
                    navController.navigate(PlayRoute)
                }
            )
        }

        composable<PlayRoute> {
            Play(viewModel = viewModel)
        }
    }
}
