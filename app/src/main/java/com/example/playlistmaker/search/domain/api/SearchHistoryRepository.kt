package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.domain.entity.Track

interface SearchHistoryRepository {
    fun getHistory():List<Track>

    fun clearHistory()

    fun addTrackToHistory(track: Track)
}