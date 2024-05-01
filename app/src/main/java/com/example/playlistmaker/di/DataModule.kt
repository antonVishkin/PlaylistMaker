package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.App.Companion.PLAYLIST_MAKER_SHARED_PREFERENCES
import com.example.playlistmaker.library.data.FavoritesRepositoryImpl
import com.example.playlistmaker.library.data.PlayListsRepositoryImpl
import com.example.playlistmaker.library.data.converters.PlayListsDBConverters
import com.example.playlistmaker.library.data.converters.TrackDBConverters
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.favorites.FavoritesRepository
import com.example.playlistmaker.library.domain.playlist.PlayListsRepository
import com.example.playlistmaker.search.data.network.ITunesApi
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApi> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com").addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(ITunesApi::class.java)
    }
    factory { Gson() }

    single {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    factory { TrackDBConverters() }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get()) }

    factory { PlayListsDBConverters() }
    single<PlayListsRepository> { PlayListsRepositoryImpl(get(),get()) }
}