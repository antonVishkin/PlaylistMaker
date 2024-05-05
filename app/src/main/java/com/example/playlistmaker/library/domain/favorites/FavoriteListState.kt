package com.example.playlistmaker.library.domain.favorites

import com.example.playlistmaker.player.domain.Track

sealed interface FavoriteListState {
    object Loading : FavoriteListState
    object Empty : FavoriteListState

    data class Content(val favoriteList: List<Track>) : FavoriteListState
}