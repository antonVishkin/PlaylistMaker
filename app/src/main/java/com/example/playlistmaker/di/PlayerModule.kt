package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.AudioPlayerImpl
import com.example.playlistmaker.player.domain.AudioPlayer
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    single { MediaPlayer() }
    single<AudioPlayer> {
        AudioPlayerImpl(get())
    }
    factory<MediaPlayerInteractor> { MediaPlayerInteractorImpl(get()) }
    viewModel {
        AudioPlayerViewModel(androidApplication(), get())
    }
}