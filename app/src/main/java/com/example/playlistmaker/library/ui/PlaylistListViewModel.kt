package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.PlaylistListState

class PlaylistListViewModel() : ViewModel() {
    private var stateLiveData =  MutableLiveData<PlaylistListState>(PlaylistListState.Empty)
    fun observeState(): LiveData<PlaylistListState> = stateLiveData

}