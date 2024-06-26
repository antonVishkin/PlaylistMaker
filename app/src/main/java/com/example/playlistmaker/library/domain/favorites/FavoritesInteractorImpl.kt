package com.example.playlistmaker.library.domain.favorites

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) :
    FavoritesInteractor {
    override suspend fun addTrackToFavorites(track: Track) {
        favoritesRepository.addTrackToFavorites(track)
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        favoritesRepository.deleteTrackFromFavorites(track)
    }

    override fun getFavorites(): Flow<List<Track>> = favoritesRepository.getFavorites()


    override suspend fun isFavorite(trackId: Long): Int {
        return favoritesRepository.isFavorite(trackId)
    }
}