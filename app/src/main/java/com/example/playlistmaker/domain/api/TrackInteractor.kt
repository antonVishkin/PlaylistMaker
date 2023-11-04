package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entity.Track

interface TrackInteractor {
    fun trackPut(track: Track)
    fun trackGet(): Track?
}