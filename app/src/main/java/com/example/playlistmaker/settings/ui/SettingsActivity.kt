package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var sharingButton: FrameLayout
    private lateinit var writeToHelpDeskButton: FrameLayout
    private lateinit var licenseAgreementButton: FrameLayout
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]
        backButtonCreate()
        sharingButtonCreate()
        helpDeskButtonCreate()
        licenseButtonCreate()
        themeSwitcherCreate()
    }

    private fun backButtonCreate() {
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            this.finish()
        }
    }

    private fun sharingButtonCreate() {
        sharingButton = findViewById(R.id.sharing_button)
        sharingButton.setOnClickListener {
            viewModel.shareLink()
        }
    }

    private fun helpDeskButtonCreate() {
        writeToHelpDeskButton = findViewById<FrameLayout>(R.id.write_help_desk)
        writeToHelpDeskButton.setOnClickListener {
            viewModel.createMailToSupport()
        }
    }

    private fun licenseButtonCreate() {
        licenseAgreementButton = findViewById(R.id.license_agreement)
        licenseAgreementButton.setOnClickListener {
            viewModel.openLicense()
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun themeSwitcherCreate() {
        val themeSwitcher = findViewById<Switch>(R.id.themeSwitcher)
        viewModel.observeDarkTheme().observe(this){
            themeSwitcher.isChecked = it
        }
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }

}