package com.example.playlistmaker.library.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.FavoriteListState
import com.example.playlistmaker.library.domain.FavoritesInteractor
import kotlinx.coroutines.launch

class FavoriteListViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteListState>(FavoriteListState.Empty)
    fun observeState(): LiveData<FavoriteListState> = stateLiveData

    init {
        fillData()
    }

    fun fillData() {
        renderState(FavoriteListState.Loading)
        viewModelScope.launch {
            favoritesInteractor
                .getFavorites()
                .collect {
                    if (it.isEmpty())
                        renderState(FavoriteListState.Empty)
                    else
                        renderState(FavoriteListState.Content(it))
                }
        }
    }

    private fun renderState(state: FavoriteListState) {
        stateLiveData.postValue(state)
    }
}