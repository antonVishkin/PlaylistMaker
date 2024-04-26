package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface TrackListRepository {
    fun searchTrack(expression: String): Flow<Result<List<Track>>>
}