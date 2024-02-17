package com.example.playlistmaker.settings.domain

class SettingsInteractorImpl(val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun saveTheme(isDark: Boolean) {
        settingsRepository.saveTheme(isDark)
    }

    override fun isDarkTheme(): Boolean {
        return settingsRepository.isDarkTheme()
    }

}