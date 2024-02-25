package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.FavoriteListState

class FavoriteListViewModel : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteListState>(FavoriteListState.Empty)
    fun observeState(): LiveData<FavoriteListState> = stateLiveData
}