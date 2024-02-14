package com.example.playlistmaker.player.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.TrackItemAdapter
import com.example.playlistmaker.creator.Creator


class AudioPlayerViewModel(application: Application):AndroidViewModel(application) {
    private var mediaPlayer = Creator.provideMediaPlayerInteractor()
    private var track = TrackItemAdapter.track

    companion object{
        fun getViewModelFactory():ViewModelProvider.Factory = viewModelFactory{
            initializer{
                AudioPlayerViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private fun preparePlayer(onPrepared:()->Unit, onCompletion:() -> Unit) {
        track?.previewUrl?.let { mediaPlayer.prepare(it, onPrepared, onCompletion) }
    }

    fun playerPause(){
        mediaPlayer.pause()
    }
}