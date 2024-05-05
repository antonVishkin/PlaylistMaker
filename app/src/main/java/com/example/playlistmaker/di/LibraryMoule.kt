package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.library.domain.favorites.FavoritesInteractorImpl
import com.example.playlistmaker.library.domain.playlist.FilesInteractor
import com.example.playlistmaker.library.domain.playlist.FilesInteractorImpl
import com.example.playlistmaker.library.domain.playlist.PlayListsInteractor
import com.example.playlistmaker.library.domain.playlist.PlayListsInteractorImpl
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.ui.favorites.FavoriteListViewModel
import com.example.playlistmaker.library.ui.playlist.PlayListCreationViewModel
import com.example.playlistmaker.library.ui.playlist.PlaylistListViewModel
import com.example.playlistmaker.library.ui.playlistdetails.PlayListEditViewModel
import com.example.playlistmaker.library.ui.playlistdetails.PlaylistDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    viewModel {
        PlaylistListViewModel(get())
    }
    viewModel {
        FavoriteListViewModel(get())
    }

    viewModel {
        PlayListCreationViewModel(get(), get())
    }

    viewModel { (playList: Playlist) -> PlaylistDetailsViewModel(playList, get(), get()) }

    viewModel { (playList: Playlist) ->
        PlayListEditViewModel(playList, get(), get())
    }

    single<FavoritesInteractor> { FavoritesInteractorImpl(get()) }

    single<PlayListsInteractor> { PlayListsInteractorImpl(get()) }

    single<FilesInteractor> { FilesInteractorImpl(get()) }

}