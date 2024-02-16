package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.data.track.Track

interface TrackListInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}