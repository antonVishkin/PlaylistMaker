package com.example.playlistmaker.library.data.db

import androidx.room.Entity

@Entity(tableName = "playlist_tracks_table", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackEntity(
    val playlistId: Int,
    val trackId: Long,
)
