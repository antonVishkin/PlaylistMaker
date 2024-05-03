package com.example.playlistmaker.library.domain.playlist

import android.net.Uri

interface FilesRepository {
    suspend fun addFileToPrivateStorage(uri: Uri):String
}