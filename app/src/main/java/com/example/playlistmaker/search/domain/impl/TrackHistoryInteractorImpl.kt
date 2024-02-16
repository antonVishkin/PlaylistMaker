package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.player.data.track.Track
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor

class TrackHistoryInteractorImpl(val searchHistoryRepository: SearchHistoryRepository) :
    TrackHistoryInteractor {
    override fun getHistory(): List<Track> {
        return searchHistoryRepository.getHistory()
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }

    override fun addTrackToHistory(track: Track) {
        searchHistoryRepository.addTrackToHistory(track)
    }
}