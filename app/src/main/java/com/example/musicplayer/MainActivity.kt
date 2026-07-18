package com.example.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.datasourse.MediaStoreDataSource
import com.example.musicplayer.repository.MusicRepository
import com.example.musicplayer.viewmodel.MusicViewmodel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            val musicViewModel: MusicViewmodel = viewModel(
                factory = remember {
                    object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            val dataSource = MediaStoreDataSource(context)
                            val repository = MusicRepository(dataSource)
                            return MusicViewmodel(repository) as T
                        }
                    }
                }
            )

            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    musicViewModel.loadSongs()
                }
            }

            val songs by musicViewModel.songs.collectAsState()
            val player = remember { ExoPlayer.Builder(context).build() }

            LaunchedEffect(Unit) {
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    musicViewModel.loadSongs()
                } else {
                    launcher.launch(permission)
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    player.release()
                }
            }

            com.example.musicplayer.navigation.NavigationC(
                songs = songs,
                player = player,
                viewModel = musicViewModel
            )
        }
    }
}