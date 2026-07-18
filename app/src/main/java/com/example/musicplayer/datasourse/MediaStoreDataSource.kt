package com.example.musicplayer.datasourse

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.musicplayer.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.net.toUri

class MediaStoreDataSource(
    private val context: Context
) {

    suspend fun getSongs(context: Context = this.context): List<Song> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<Song>()

        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )

        val selection =
            "${MediaStore.Audio.Media.DURATION} >= ?"

        context.contentResolver.query(
            collection,
            projection,
            selection,
            arrayOf("10000"),
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {

                val id = cursor.getLong(idColumn)

                val title = cursor.getString(titleColumn)

                val artist = cursor.getString(artistColumn)

                val album = cursor.getString(albumColumn)

                val duration = cursor.getLong(durationColumn)

                val albumId = cursor.getLong(albumIdColumn)

                val songUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val artworkUri = ContentUris.withAppendedId(
                    "content://media/external/audio/albumart".toUri(),
                    albumId
                )

                songs.add(
                    Song(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        uri = songUri,
                        artworkUri = artworkUri
                    )
                )
            }
        }

        songs
    }

}