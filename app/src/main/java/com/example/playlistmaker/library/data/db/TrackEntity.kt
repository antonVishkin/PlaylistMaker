package com.example.playlistmaker.library.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_favorites")
data class TrackEntity(
    @PrimaryKey
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val artworkUrl100: String,
    val country: String,
    val previewUrl: String,
)