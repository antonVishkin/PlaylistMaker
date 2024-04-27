package com.example.playlistmaker.player.domain


import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId:Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val artworkUrl100: String,
    val country: String,
    val previewUrl: String
) : Parcelable {
    var isFavorite: Boolean = false
    companion object {
        const val TRACK = "TRACK_PARCELABLE_EXTRA"
    }
}