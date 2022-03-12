package com.example.musicplayer.data


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.Q)
    fun initializeListIfNeeded(context: Context) {
        if (initialized) return

        val treeUri = Uri.parse(
            context.getSharedPreferences("settings", 0)
                .getString("path", null)
        )
        val contract = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri))
        val volumeNames: Set<String> = MediaStore.getExternalVolumeNames(context)
        val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)


        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.YEAR,
        )
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
//        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
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




            while (cursor.moveToNext()) {
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val duration = cursor.getLong(durationColumn)
                val year = cursor.getInt(yearColumn)
//                val artist = cursor.getString(artistColumn)
                Log.d("SongListViewModel", "Found song: $title")

                musicCards.add(
                    SongModel(
                        title = title,
                        artist = artist,
                        duration = duration,
                        album = album,
                        year = year,
                        uri = null,
                        image = null
                    )
                )
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
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context, uri)
        val data = mmr.embeddedPicture
        return if(data != null){
            BitmapFactory.decodeByteArray(data, 0, data.size)
        } else{
            BitmapFactory.decodeResource(context.resources, R.drawable.note)
        }
    }
}