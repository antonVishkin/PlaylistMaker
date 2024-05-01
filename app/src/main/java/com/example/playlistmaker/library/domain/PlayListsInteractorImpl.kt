package com.example.playlistmaker.library.domain

import kotlinx.coroutines.flow.Flow

class PlayListsInteractorImpl(private val playListsInteractor: PlayListsInteractor) :
    PlayListsInteractor {
    override suspend fun addPlayList(playlist: Playlist) {
        playListsInteractor.addPlayList(playlist)
    }

    override suspend fun getPlaylistsList(): Flow<List<Playlist>> =
        playListsInteractor.getPlaylistsList()
}