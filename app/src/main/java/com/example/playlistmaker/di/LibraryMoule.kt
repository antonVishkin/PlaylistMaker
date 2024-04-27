package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.FavoritesInteractor
import com.example.playlistmaker.library.domain.FavoritesInteractorImpl
import com.example.playlistmaker.library.ui.FavoriteListViewModel
import com.example.playlistmaker.library.ui.PlaylistListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    viewModel {
        PlaylistListViewModel()
    }
    viewModel {
        FavoriteListViewModel()
    }

    single <FavoritesInteractor> { FavoritesInteractorImpl(get()) }

}