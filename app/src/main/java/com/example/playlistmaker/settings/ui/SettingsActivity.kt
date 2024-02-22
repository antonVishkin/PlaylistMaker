package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var sharingButton: FrameLayout
    private lateinit var writeToHelpDeskButton: FrameLayout
    private lateinit var licenseAgreementButton: FrameLayout
    private val viewModel: SettingsViewModel by viewModel {
        parametersOf(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
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

    private fun themeSwitcherCreate() {
        val themeSwitcher = findViewById<Switch>(R.id.themeSwitcher)
        themeSwitcher.isChecked = viewModel.observeDarkTheme().value ?: false
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }

}