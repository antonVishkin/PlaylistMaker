package com.example.playlistmaker.library.domain.playlist

import android.net.Uri

class FilesInteractorImpl(private val filesRepository: FilesRepository):FilesInteractor {
    override suspend fun addFileToPrivateStorage(uri: Uri): String {
        return filesRepository.addFileToPrivateStorage(uri)
    }
}