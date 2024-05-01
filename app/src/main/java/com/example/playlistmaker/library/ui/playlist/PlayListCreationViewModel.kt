package com.example.playlistmaker.library.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.playlist.PlayListsInteractor
import com.example.playlistmaker.library.domain.playlist.Playlist
import kotlinx.coroutines.launch

class PlayListCreationViewModel(private val playListsInteractor: PlayListsInteractor) :
    ViewModel() {

    fun createPlaylist(name: String, description: String, imagePath: String) {
        viewModelScope.launch {
            playListsInteractor.addPlayList(
                Playlist(
                    name,
                    description,
                    imagePath,
                    listOf()
                )
            )
        }
    }
}