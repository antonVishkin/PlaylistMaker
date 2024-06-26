package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlayListsRepository {
    suspend fun addPlayList(name: String, description: String, imagePath: String)

    suspend fun getPlaylistsList(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean

    suspend fun removeTrackFromPlaylist(track: Track,playlist: Playlist):Playlist

    suspend fun removePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun getPlayListById(playlistId: Int):Playlist
}