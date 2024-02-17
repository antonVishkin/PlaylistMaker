package com.example.playlistmaker.settings.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.domain.EmailData

class SettingsViewModel(private val application: Application) :
    AndroidViewModel(application) {
    private val settingsInteractor = Creator.provideSettingsInteractor(application)
    private val sharingInteractor = Creator.provideSharingInteractor(application)
    private val darkThemeLivedata = MutableLiveData(settingsInteractor.isDarkTheme())
    fun observeDarkTheme() = darkThemeLivedata

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application,

                    )
            }
        }
    }

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