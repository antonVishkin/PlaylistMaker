package com.example.playlistmaker.player.domain

import com.example.playlistmaker.domain.entity.Track

interface TrackInteractor {
    fun trackPut(track: Track)
    fun trackGet(): Track?
}