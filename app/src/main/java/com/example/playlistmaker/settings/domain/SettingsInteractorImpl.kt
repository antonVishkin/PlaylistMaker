package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.data.SettingsRepository

class SettingsInteractorImpl(val settingsRepository: SettingsRepository):SettingsInteractor {
    override fun saveTheme(isDark: Boolean) {
        settingsRepository.saveTheme(isDark)
    }

    override fun isDarkTheme(): Boolean {
        return settingsRepository.isDarkTheme()
    }

}