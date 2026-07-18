package com.example.musicplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.model.Song
import com.example.musicplayer.repository.MusicRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MusicViewmodel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration = _duration.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private var progressJob: Job? = null

    fun loadSongs() {
        viewModelScope.launch {
            _songs.value = repository.getSongs()
        }
    }

    fun setCurrentSong(song: Song) {
        _currentSong.value = song
    }

    fun startProgressUpdate(player: ExoPlayer) {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive) {
                _currentPosition.value = player.currentPosition
                _duration.value = player.duration.coerceAtLeast(0L)
                _isPlaying.value = player.isPlaying
                delay(500.milliseconds)
            }
        }
    }

    fun nextSong() {
        val currentList = _songs.value
        val currentIndex = currentList.indexOf(_currentSong.value)
        if (currentIndex != -1 && currentIndex < currentList.size - 1) {
            _currentSong.value = currentList[currentIndex + 1]
        } else if (currentList.isNotEmpty()) {
            _currentSong.value = currentList[0]
        }
    }

    fun previousSong() {
        val currentList = _songs.value
        val currentIndex = currentList.indexOf(_currentSong.value)
        if (currentIndex != -1 && currentIndex > 0) {
            _currentSong.value = currentList[currentIndex - 1]
        } else if (currentList.isNotEmpty()) {
            _currentSong.value = currentList.last()
        }
    }
}
