package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.entity.Track

class TrackInteractorImpl(private val trackRepository: TrackRepository) : TrackInteractor {
    override fun trackPut(track: Track) {
        trackRepository.putTrack(track)
    }

    override fun trackGet(): Track? {
        return trackRepository.getTrack()
    }

}