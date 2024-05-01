package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.library.domain.favorites.FavoritesInteractorImpl
import com.example.playlistmaker.library.ui.favorites.FavoriteListViewModel
import com.example.playlistmaker.library.ui.playlist.PlaylistListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    viewModel {
        PlaylistListViewModel()
    }
    viewModel {
        FavoriteListViewModel(get())
    }

    single<FavoritesInteractor> { FavoritesInteractorImpl(get()) }

}