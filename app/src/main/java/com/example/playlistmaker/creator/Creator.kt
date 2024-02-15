package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.mediaplayer.AudioPlayerImpl
import com.example.playlistmaker.player.data.prefs.TrackRepositoryImpl
import com.example.playlistmaker.player.data.mediaplayer.api.AudioPlayer
import com.example.playlistmaker.player.data.mediaplayer.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.TrackInteractor
import com.example.playlistmaker.player.data.prefs.api.TrackRepository
import com.example.playlistmaker.player.data.mediaplayer.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.TrackInteractorImpl
import com.example.playlistmaker.search.data.TrackListRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.domain.api.TrackListRepository
import com.example.playlistmaker.search.domain.impl.TrackListInteractorImpl

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

    private fun getTrackListRepository():TrackListRepository{
        return TrackListRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackListInteractor():TrackListInteractor{
        return TrackListInteractorImpl(getTrackListRepository())
    }
}