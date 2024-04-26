package com.example.playlistmaker.search.domain.impl

import com.bumptech.glide.load.engine.Resource
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.domain.api.TrackListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackListInteractorImpl(private val repository: TrackListRepository) : TrackListInteractor {
    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTrack(expression).map {result ->
            if (result.isSuccess) {
                Pair(result.getOrNull(),null)
            } else {
                Pair(null,result.exceptionOrNull()?.message)
            }
        }
    }
}