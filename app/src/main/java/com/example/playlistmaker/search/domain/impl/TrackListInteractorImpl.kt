package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.domain.api.TrackListRepository
import java.util.concurrent.Executors

class TrackListInteractorImpl(private val repository: TrackListRepository) : TrackListInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TrackListInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTrack(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }
}