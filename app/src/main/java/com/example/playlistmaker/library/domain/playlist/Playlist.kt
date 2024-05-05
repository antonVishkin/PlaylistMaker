package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.player.domain.Track

data class Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val imagePath: String,
    var list: List<Track>
)
