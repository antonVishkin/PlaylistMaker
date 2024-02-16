package com.example.playlistmaker.settings.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.SearchViewModel

class SettingsViewModel(val application: Application) : AndroidViewModel(application) {

    private val darkThemeLivedata = MutableLiveData<Boolean>(false)
    fun observeDarkTheme() = darkThemeLivedata
    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    fun shareLink(){
        val sharingButtonIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(
                Intent.EXTRA_TEXT,
                application.getString(R.string.help_desk_link_to_curse)
            )
            type = "text/plain"
        }
        application.startActivity(sharingButtonIntent)
    }

    fun createMailToSupport(){
        val title = application.getString(R.string.help_desk_post_title)
        val message = application.getString(R.string.help_desk_post_text)
        val email = application.getString(R.string.help_desk_post_email)
        val writeToHelpDeskIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        application.startActivity(writeToHelpDeskIntent)
    }

    fun openLicense(){
        val licenseAgreementIntent = Intent(Intent.ACTION_VIEW)
        licenseAgreementIntent.data = Uri.parse(application.getString(R.string.help_desk_link_to_license))
        application.startActivity(licenseAgreementIntent)
    }

    fun switchTheme(checked:Boolean){
        (application as App).switchTheme(checked)
    }

}