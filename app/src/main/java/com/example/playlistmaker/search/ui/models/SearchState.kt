package com.example.playlistmaker.search.ui.models

import com.example.playlistmaker.domain.entity.Track

sealed interface SearchState{
    object Loading : SearchState
    data class Results(val trackList: List<Track>):SearchState
    data class History(val trackList: List<Track>):SearchState
    object Empty:SearchState
    object NoInternet:SearchState
}