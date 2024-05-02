package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.converters.PlayListsDBConverters
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.PlayListsEntity
import com.example.playlistmaker.library.data.db.PlaylistTrackEntity
import com.example.playlistmaker.library.domain.playlist.PlayListsRepository
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListsDBConverters: PlayListsDBConverters
) : PlayListsRepository {
    override suspend fun addPlayList(name: String, description: String, imagePath: String) {
        appDatabase.playListsDao().addPlayList(
            PlayListsEntity(
                id = 0,
                name = name,
                description = description,
                imageFilePath = imagePath
            )
        )
    }

    override suspend fun getPlaylistsList(): Flow<List<Playlist>> = flow {
        val playListsList = appDatabase.playListsDao().getPlayListsList().map {
            val trackList = appDatabase.playListTrackDao().getTracksByPlaylistId(it.id)
            playListsDBConverters.map(it, trackList)
        }
        emit(playListsList)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        try {
            appDatabase.playListTrackDao()
                .addTrackToPlaylist(PlaylistTrackEntity(playlist.id, track.trackId))
            return true
        } catch (e: Throwable) {
            return false
        }
    }


}