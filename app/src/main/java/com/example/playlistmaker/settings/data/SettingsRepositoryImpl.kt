package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.App.Companion.THEME_KEY
import com.example.playlistmaker.settings.domain.SettingsRepository


class SettingsRepositoryImpl(context: Context, private val sharedPreference: SharedPreferences) :
    SettingsRepository {


    override fun saveTheme(isDark: Boolean) {
        sharedPreference.edit().putBoolean(THEME_KEY, isDark).apply()
    }

    override fun isDarkTheme(): Boolean {
        return sharedPreference.getBoolean(
            THEME_KEY, false
        )
    }
}