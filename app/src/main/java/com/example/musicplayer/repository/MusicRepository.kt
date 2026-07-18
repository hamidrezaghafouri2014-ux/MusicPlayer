package com.example.musicplayer.repository

import com.example.musicplayer.datasourse.MediaStoreDataSource
import com.example.musicplayer.model.Song

class MusicRepository(
    private val dataSource: MediaStoreDataSource
) {

    suspend fun getSongs(): List<Song> {
        return dataSource.getSongs()
    }

}