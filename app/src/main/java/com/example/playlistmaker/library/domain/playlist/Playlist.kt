package com.example.playlistmaker.library.domain.playlist

data class Playlist(
    val id:Int,
    val name: String,
    val description: String,
    val imagePath: String,
    var list: List<Int>
)