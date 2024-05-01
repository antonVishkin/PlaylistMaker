package com.example.playlistmaker.library.domain.playlist

import kotlinx.coroutines.flow.Flow

interface PlayListsInteractor {
    suspend fun addPlayList(playlist: Playlist)

    suspend fun getPlaylistsList(): Flow<List<Playlist>>
}