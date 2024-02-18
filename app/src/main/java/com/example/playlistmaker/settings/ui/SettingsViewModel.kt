package com.example.playlistmaker.settings.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.EmailData
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val application: Application, private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) :
    AndroidViewModel(application) {
    private val darkThemeLivedata = MutableLiveData(settingsInteractor.isDarkTheme())
    fun observeDarkTheme() = darkThemeLivedata


    fun shareLink() {
        sharingInteractor.shareApp(application.getString(R.string.share_application))
    }

    fun createMailToSupport() {
        sharingInteractor.openSupport(
            EmailData(
                arrayOf(application.getString(R.string.help_desk_post_email)),
                application.getString(R.string.help_desk_post_title),
                application.getString(R.string.help_desk_post_text)
            )
        )
    }

    fun openLicense() {
        sharingInteractor.openTerms(application.getString(R.string.help_desk_link_to_license))
    }

    fun switchTheme(checked: Boolean) {
        settingsInteractor.saveTheme(checked)
        AppCompatDelegate.setDefaultNightMode(
            if (checked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}