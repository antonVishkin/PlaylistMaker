package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun saveTheme(isDark: Boolean)

    fun isDarkTheme(): Boolean
}