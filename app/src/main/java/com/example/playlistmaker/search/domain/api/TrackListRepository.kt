package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.Track

interface TrackListRepository {
    fun searchTrack(expression: String): Result<List<Track>>
}