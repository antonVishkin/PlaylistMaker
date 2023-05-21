package com.example.playlistmaker

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName:String,
    val releaseDate:String,
    val primaryGenreName:String,
    val artworkUrl100: String,
    val country:String
)
