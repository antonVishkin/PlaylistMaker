package com.example.playlistmaker.library.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.playlist.PlayListsInteractor
import com.example.playlistmaker.library.domain.playlist.PlaylistListState
import kotlinx.coroutines.launch

class PlaylistListViewModel(private val playListsInteractor: PlayListsInteractor) : ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistListState>(PlaylistListState.Empty)
    fun observeState(): LiveData<PlaylistListState> = stateLiveData
    init {
        fillData()
    }
    fun fillData(){
        renderState(PlaylistListState.Loading)

    }

    private fun renderState(state: PlaylistListState){
        stateLiveData.postValue(state)
        viewModelScope.launch {
            playListsInteractor.getPlaylistsList().collect{
                if (it.isEmpty())
                    renderState(PlaylistListState.Empty)
                else
                    renderState(PlaylistListState.Content(it))
            }
        }
    }

}