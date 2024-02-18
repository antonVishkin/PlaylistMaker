package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun saveTheme(isDark: Boolean)

    fun isDarkTheme(): Boolean
}