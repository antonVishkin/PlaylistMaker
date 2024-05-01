package com.example.playlistmaker.library.domain.playlist

import kotlinx.coroutines.flow.Flow

class PlayListsInteractorImpl(private val playListsRepository: PlayListsRepository) :
    PlayListsInteractor {
    override suspend fun addPlayList(playlist: Playlist) {
        playListsRepository.addPlayList(playlist)
    }

    override suspend fun getPlaylistsList(): Flow<List<Playlist>> =
        playListsRepository.getPlaylistsList()
}