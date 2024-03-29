package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.domain.api.TrackListRepository

class TrackListRepositoryImpl(private val networkClient: NetworkClient) : TrackListRepository {
    override fun searchTrack(expression: String): Result<List<Track>> {
        val response = networkClient.doRequest(SearchRequest(expression))
        return when (response.resultCode) {
            200 ->
                Result.success((response as SearchResponse).results.map {
                    Track(
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.artworkUrl100,
                        it.country,
                        it.previewUrl
                    )
                })

            else -> {
                Result.failure(Throwable("Сетевая ошибка"))
            }
        }
    }
}