package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.Track
import kotlinx.coroutines.flow.Flow

interface TrackListInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

}