package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SearchHistoryProvider
import com.example.playlistmaker.search.data.TrackListRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.domain.api.TrackListRepository
import com.example.playlistmaker.search.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackListInteractorImpl
import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    single<NetworkClient> { RetrofitNetworkClient() }
    single<SearchHistoryRepository> { SearchHistoryProvider(androidApplication()) }
    single<TrackListRepository> { TrackListRepositoryImpl(get()) }
    factory<TrackHistoryInteractor> { TrackHistoryInteractorImpl(get()) }
    factory<TrackListInteractor> { TrackListInteractorImpl(get()) }
    viewModel<SearchViewModel> {
        SearchViewModel(androidApplication(), get(), get())
    }
}