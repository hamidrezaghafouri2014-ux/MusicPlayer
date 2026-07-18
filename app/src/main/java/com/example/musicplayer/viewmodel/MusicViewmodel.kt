package com.example.musicplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.model.Song
import com.example.musicplayer.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewmodel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    fun loadSongs() {
        viewModelScope.launch {
            _songs.value = repository.getSongs()
        }
    }

    fun setCurrentSong(song: Song) {
        _currentSong.value = song
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