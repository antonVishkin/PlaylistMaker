package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.mediaplayer.AudioPlayerImpl
import com.example.playlistmaker.player.data.prefs.TrackRepositoryImpl
import com.example.playlistmaker.player.data.mediaplayer.api.AudioPlayer
import com.example.playlistmaker.player.data.mediaplayer.api.MediaPlayerInteractor
import com.example.playlistmaker.player.data.prefs.api.TrackInteractor
import com.example.playlistmaker.player.data.prefs.api.TrackRepository
import com.example.playlistmaker.player.data.mediaplayer.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.data.prefs.impl.TrackInteractorImpl

object Creator {
    private fun getAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl()
    }

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(context = context)
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getAudioPlayer())
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context = context))
    }
}