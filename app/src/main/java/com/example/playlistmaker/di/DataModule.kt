package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.App.Companion.PLAYLIST_MAKER_SHARED_PREFERENCES
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
}