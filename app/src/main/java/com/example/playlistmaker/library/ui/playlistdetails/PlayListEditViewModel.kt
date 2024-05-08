package com.example.playlistmaker.library.ui.playlistdetails

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.playlist.FilesInteractor
import com.example.playlistmaker.library.domain.playlist.PlayListsInteractor
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.ui.playlist.PlayListCreationViewModel
import kotlinx.coroutines.launch

class PlayListEditViewModel(
                            private val playList:Playlist,
                            private val playListsInteractor: PlayListsInteractor,
                            private val filesInteractor: FilesInteractor
) : PlayListCreationViewModel(
    playListsInteractor, filesInteractor
) {
    private val playListLiveData = MutableLiveData<Playlist>()
    fun observePlayList():LiveData<Playlist> = playListLiveData

    override fun createPlaylist(name: String, description: String, imageUri: Uri?) {
        viewModelScope.launch {
            val imagePath = if (imageUri != null) filesInteractor.addFileToPrivateStorage(imageUri) else ""
            playListsInteractor.updatePlaylist(Playlist(playList.id,name,description,imagePath,playList.list))
        }
    }
}