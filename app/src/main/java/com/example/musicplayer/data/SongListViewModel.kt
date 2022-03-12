package com.example.musicplayer.data


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus


class SongListViewModel : ViewModel() {
    val musicCards = mutableStateListOf<SongModel>()

    private var initialized = false
    private val backgroundScope = viewModelScope.plus(Dispatchers.Default)

    fun initializeListIfNeeded(context: Context) {
        if (initialized) return

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media._ID,
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"

        val query = collection?.let {
            context.contentResolver.query(
                it,
                projection,
                selection,
                null,
                null
            )
        }
        DocumentsContract.Document.COLUMN_DISPLAY_NAME
        query?.use { cursor ->
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val yearColumn = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)
            val isMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)
            val id = cursor.getColumnIndex(MediaStore.Audio.Media._ID)

            while (cursor.moveToNext()) {
                if(cursor.getInt(isMusic) == 0) continue
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val duration = cursor.getLong(durationColumn)
                val year = cursor.getInt(yearColumn)
                val uri = Uri.withAppendedPath(collection, cursor.getString(id))
                Log.d("SongListViewModel", "Found song: $title")

                if(artist != "<unknown>") {
                    musicCards.add(
                        SongModel(
                            title = title,
                            artist = artist,
                            duration = duration,
                            album = album,
                            year = year,
                            uri = uri,
                            image = null
                        )
                    )
                }
            }
        }
        initialized = true
    }

    fun loadBitmapIfNeeded(context: Context, index: Int) {
        if (musicCards[index].image != null) return
        // if this is gonna lag during scrolling, you can move it on a background thread
        backgroundScope.launch {
            val bitmap = musicCards[index].uri?.let { getAlbumArt(context, it) }
            musicCards[index] = musicCards[index].copy(image = bitmap)
        }
    }

    private fun getAlbumArt(context: Context, uri: Uri): Bitmap {
        val thumbnail: Bitmap =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    context.contentResolver.loadThumbnail(uri, Size(128, 128), null)
                } catch (e: Exception) {
                    BitmapFactory.decodeResource(context.resources, R.drawable.back)
                }
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.back)
            }
        return thumbnail
    }

    fun isBitmapLoaded(index: Int): Boolean {
        return musicCards[index].image != null
    }
}