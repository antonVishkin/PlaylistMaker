package com.example.playlistmaker.library.ui.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.playlist.FilesInteractor
import com.example.playlistmaker.library.domain.playlist.PlayListsInteractor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

open class PlayListCreationViewModel(
    private val playListsInteractor: PlayListsInteractor,
    private val filesInteractor: FilesInteractor,
) :
    ViewModel() {

    open fun createPlaylist(name: String, description: String, imageUri: Uri?) {
        viewModelScope.launch {
            val imagePath = if (imageUri != null) filesInteractor.addFileToPrivateStorage(imageUri) else ""
            playListsInteractor.addPlayList(
                name = name, description = description, imagePath = imagePath
            )
        }
    }

}