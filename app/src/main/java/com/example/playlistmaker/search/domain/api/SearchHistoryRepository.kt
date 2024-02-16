package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.data.track.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>

    fun clearHistory()

    fun addTrackToHistory(track: Track)
}