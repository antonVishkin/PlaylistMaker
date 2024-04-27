package com.example.playlistmaker.library.domain

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrackToFavorites(track: Track)

    fun getFavorites(): Flow<List<Track>>
}