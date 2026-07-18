package com.example.musicplayer.model

import android.content.ContentUris
import android.net.Uri
import androidx.annotation.DrawableRes

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: Uri,
    val artworkUri: Uri?
)