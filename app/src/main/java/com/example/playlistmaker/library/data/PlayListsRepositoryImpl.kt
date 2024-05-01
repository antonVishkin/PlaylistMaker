package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.converters.PlayListsDBConverters
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.playlist.PlayListsRepository
import com.example.playlistmaker.library.domain.playlist.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListsDBConverters: PlayListsDBConverters
) : PlayListsRepository {
    override suspend fun addPlayList(playlist: Playlist) {
        appDatabase.playListsDao().addPlayList(playListsDBConverters.map(playlist))
    }

    override suspend fun getPlaylistsList(): Flow<List<Playlist>> = flow {
        val playListsList = appDatabase.playListsDao().getPlayListsList().map {
            val trackList = appDatabase.playListTrackDao().getTracksByPlaylistId(it.id)
            playListsDBConverters.map(it, trackList)
        }
        emit(playListsList)
    }


}