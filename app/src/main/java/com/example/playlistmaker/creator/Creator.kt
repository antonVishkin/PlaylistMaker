package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.AudioPlayerImpl
import com.example.playlistmaker.player.domain.AudioPlayer
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
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl

object Creator {
    private fun getAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl()
    }


    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getAudioPlayer())
    }

    private fun getTrackListRepository(): TrackListRepository {
        return TrackListRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackListInteractor(): TrackListInteractor {
        return TrackListInteractorImpl(getTrackListRepository())
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryProvider(context)
    }

    fun provideTrackHistoryInteractor(context: Context): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }
}