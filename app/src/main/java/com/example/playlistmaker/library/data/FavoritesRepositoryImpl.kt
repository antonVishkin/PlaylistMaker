package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.converters.TrackDBConverters
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.TrackEntity
import com.example.playlistmaker.library.domain.FavoritesRepository
import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDBConverters: TrackDBConverters
) : FavoritesRepository {
    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.trackDao().addTrackToFavorite(trackDBConverters.map(track))
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        val trackEntity = appDatabase.trackDao().getFavoriteById(track.trackId)
        appDatabase.trackDao().removeTrackFromFavorites(trackEntity)
    }

    override suspend fun isFavorite(trackId: Long): Int {
        return appDatabase.trackDao().isFavorite(trackId)
    }

    override fun getFavorites(): Flow<List<Track>> = flow {
        val trackList = appDatabase.trackDao().getFavoriteList().sortedByDescending { it.indexAdding }
        emit(convertFromTrackEntity(trackList))
    }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDBConverters.map(track) }
    }
}