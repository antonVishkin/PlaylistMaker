package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

class PlayListsInteractorImpl(private val playListsRepository: PlayListsRepository) :
    PlayListsInteractor {
    override suspend fun addPlayList(name: String, description: String, imagePath: String) {
        playListsRepository.addPlayList(name, description, imagePath)
    }

    override suspend fun getPlaylistsList(): Flow<List<Playlist>> =
        playListsRepository.getPlaylistsList()

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track){
        playListsRepository.addTrackToPlaylist(playlist,track)
    }
}