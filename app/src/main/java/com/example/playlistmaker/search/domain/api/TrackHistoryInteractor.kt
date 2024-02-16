package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.data.track.Track

interface TrackHistoryInteractor {
    fun getHistory():List<Track>

    fun clearHistory()

    fun addTrackToHistory(track: Track)
}