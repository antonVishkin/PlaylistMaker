package com.example.playlistmaker.player.data.prefs.api

import com.example.playlistmaker.domain.entity.Track

interface TrackInteractor {
    fun trackPut(track: Track)
    fun trackGet(): Track?
}