package com.example.playlistmaker.library.domain

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addTrackToFavorites(track: Track)

    suspend fun deleteTrackFromFavorites(track: Track)

    suspend fun getFavorites(): Flow<List<Track>>

    suspend fun isFavorite(trackId: Long): Int
}