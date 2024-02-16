package com.example.playlistmaker.settings.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.App.Companion.PLAYLIST_MAKER_SHARED_PREFERENCES
import com.example.playlistmaker.App.Companion.THEME_KEY


class SettingsRepositoryImpl(context: Context):SettingsRepository {
    private var sharedPreference: SharedPreferences = context.getSharedPreferences(
        PLAYLIST_MAKER_SHARED_PREFERENCES,
        Application.MODE_PRIVATE
    )

    override fun saveTheme(isDark: Boolean) {
        sharedPreference.edit().putBoolean(THEME_KEY, isDark).apply()
    }

    override fun isDarkTheme(): Boolean {
        return sharedPreference.getBoolean(
            THEME_KEY,false)
    }
}