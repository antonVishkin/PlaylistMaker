package com.example.playlistmaker.library.domain.playlistdetails

import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistListState

sealed interface PlaylistDetailsState {
    object Empty : PlaylistDetailsState
    data class Content(val playlist: Playlist):PlaylistDetailsState
}