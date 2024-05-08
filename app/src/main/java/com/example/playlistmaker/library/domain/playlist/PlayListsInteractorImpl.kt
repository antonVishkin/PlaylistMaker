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

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        return playListsRepository.addTrackToPlaylist(playlist, track)
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist):Playlist {
        return playListsRepository.removeTrackFromPlaylist(track, playlist)
    }

    override suspend fun removePlaylist(playlist: Playlist){
        playListsRepository.removePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playListsRepository.updatePlaylist(playlist)
    }

    override suspend fun getPlayListById(playlistId: Int): Playlist {
        return playListsRepository.getPlayListById(playlistId)
    }


}