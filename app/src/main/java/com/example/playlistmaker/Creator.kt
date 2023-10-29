package com.example.playlistmaker

import com.example.playlistmaker.data.mediaplayer.AudioPlayerImpl
import com.example.playlistmaker.domain.api.AudioPlayer
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl

object Creator{
    private fun getAudioPlayer():AudioPlayer{
        return AudioPlayerImpl()
    }
    fun provideMediaPlayerInteractor(): MediaPlayerInteractor{
        return MediaPlayerInteractorImpl(getAudioPlayer())
    }
}