package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.library.data.db.TrackEntity
import com.example.playlistmaker.player.domain.Track

class TrackDBConverters {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            indexAdding = 0,
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            artworkUrl100 = track.artworkUrl100,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            artworkUrl100 = track.artworkUrl100,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}