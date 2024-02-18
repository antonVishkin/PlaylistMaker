package com.example.playlistmaker.di

import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingModule = module {
    factory<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }
    factory<SharingInteractor> { SharingInteractorImpl(get()) }
}