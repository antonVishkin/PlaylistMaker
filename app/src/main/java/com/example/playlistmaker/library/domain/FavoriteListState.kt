package com.example.playlistmaker.library.domain

sealed interface FavoriteListState {
    object Loading : FavoriteListState
    object Empty : FavoriteListState

    data class Content(val favoriteList: Playlist) : FavoriteListState
}