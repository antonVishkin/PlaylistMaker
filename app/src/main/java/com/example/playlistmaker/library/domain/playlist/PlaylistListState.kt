package com.example.playlistmaker.library.domain.playlist

sealed interface PlaylistListState {
    object Loading : PlaylistListState
    object Empty : PlaylistListState
    data class Content(val playlistList: List<Playlist>) : PlaylistListState
}