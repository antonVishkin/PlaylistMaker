package com.example.playlistmaker.library.domain.playlist

import android.net.Uri

interface FilesInteractor {
    suspend fun addFileToPrivateStorage(uri: Uri):String

}