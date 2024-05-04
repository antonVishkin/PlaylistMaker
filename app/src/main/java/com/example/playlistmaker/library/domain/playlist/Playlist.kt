package com.example.playlistmaker.library.domain.playlist

import android.os.Parcelable
import com.example.playlistmaker.player.domain.Track
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val imagePath: String,
    var list: List<Track>
) : Parcelable
