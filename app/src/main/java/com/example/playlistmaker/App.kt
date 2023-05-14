package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_SHARED_PREFERENCES = "playlist_maker_shared_preferences"
const val THEME_KEY = "theme_key"

class App : Application() {
    private lateinit var sharedPreference:SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreference = getSharedPreferences(PLAYLIST_MAKER_SHARED_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreference.getBoolean(
            THEME_KEY,
            darkTheme
        )
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPreference.edit().putBoolean(THEME_KEY, darkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        var darkTheme = false
    }

}