package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.domain.api.TrackListRepository
import java.util.concurrent.Executors

class TrackListInteractorImpl(private val repository: TrackListRepository) : TrackListInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TrackListInteractor.TracksConsumer) {
        executor.execute {
            val resource = repository.searchTrack(expression)
            if (resource.isSuccess) {
                consumer.consume(resource.getOrNull(), null)
            } else {
                consumer.consume(null, resource.exceptionOrNull()?.message)
            }
        }
    }
}