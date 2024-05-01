package com.example.playlistmaker.library.ui.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.playlist.PlayListsInteractor
import com.example.playlistmaker.library.domain.playlist.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PlayListCreationViewModel(
    private val context: Context,
    private val playListsInteractor: PlayListsInteractor
) :
    ViewModel() {

    fun createPlaylist(name: String, description: String, imageUri: Uri?) {
        viewModelScope.launch {
            val imagePath = if (imageUri != null) saveImageToPrivateStorage(imageUri) else ""
            playListsInteractor.addPlayList(
                Playlist(
                    name,
                    description,
                    imagePath,
                    listOf()
                )
            )
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists images")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${System.currentTimeMillis()}.jpg")
        val inputStream = context.contentResolver?.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.absolutePath
    }
}