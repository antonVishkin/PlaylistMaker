package com.example.playlistmaker.settings.data

interface SettingsRepository {
    fun saveTheme(isDark: Boolean)

    fun isDarkTheme():Boolean
}