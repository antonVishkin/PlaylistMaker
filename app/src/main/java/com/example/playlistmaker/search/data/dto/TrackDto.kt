package com.example.playlistmaker.search.data.dto

data class TrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val artworkUrl100: String,
    val country: String,
    val previewUrl: String
)
