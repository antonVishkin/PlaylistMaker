package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.domain.entity.Track

interface TrackListInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks:List<Track>?,errorMessage:String?)
    }
}