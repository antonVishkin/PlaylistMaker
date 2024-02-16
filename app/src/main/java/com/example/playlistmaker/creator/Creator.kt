package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.mediaplayer.impl.AudioPlayerImpl
import com.example.playlistmaker.player.data.mediaplayer.api.AudioPlayer
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.data.SearchHistoryProvider
import com.example.playlistmaker.search.data.TrackListRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.domain.api.TrackListRepository
import com.example.playlistmaker.search.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackListInteractorImpl

object Creator {
    private fun getAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl()
    }


    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getAudioPlayer())
    }

    private fun getTrackListRepository():TrackListRepository{
        return TrackListRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackListInteractor():TrackListInteractor{
        return TrackListInteractorImpl(getTrackListRepository())
    }

    private fun getSearchHistoryRepository(context: Context):SearchHistoryRepository{
        return SearchHistoryProvider(context)
    }

    fun provideTrackHistoryInteractor(context: Context):TrackHistoryInteractor{
        return TrackHistoryInteractorImpl(getSearchHistoryRepository(context))
    }
}