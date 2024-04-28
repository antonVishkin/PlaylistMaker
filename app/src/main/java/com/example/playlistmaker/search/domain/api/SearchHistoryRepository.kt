package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getHistory(): Flow<List<Track>>

    fun clearHistory()

    fun addTrackToHistory(track: Track)
}