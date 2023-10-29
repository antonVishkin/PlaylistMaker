package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.mediaplayer.AudioPlayerImpl
import com.example.playlistmaker.data.prefs.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.AudioPlayer
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {
    private fun getAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl()
    }

    private fun getTrackRepository(context: Context): TrackRepository {
        val preferencesRepository = TrackRepositoryImpl()
        preferencesRepository.init(context)
        return preferencesRepository
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getAudioPlayer())
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context = context))
    }
}