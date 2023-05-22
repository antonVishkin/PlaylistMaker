package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var sharingButton: FrameLayout
    private lateinit var writeToHelpDeskButton: FrameLayout
    private lateinit var licenseAgreementButton: FrameLayout
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
            val sharingButtonIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.help_desk_link_to_curse)
                )
                type = "text/plain"
            }
            startActivity(sharingButtonIntent)
        }
    }

    private fun helpDeskButtonCreate() {
        writeToHelpDeskButton = findViewById<FrameLayout>(R.id.write_help_desk)
        writeToHelpDeskButton.setOnClickListener {
            val title = getString(R.string.help_desk_post_title)
            val message = getString(R.string.help_desk_post_text)
            val email = getString(R.string.help_desk_post_email)
            val writeToHelpDeskIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(writeToHelpDeskIntent)
        }
    }

    private fun licenseButtonCreate() {
        licenseAgreementButton = findViewById(R.id.license_agreement)
        licenseAgreementButton.setOnClickListener {
            val licenseAgreementIntent = Intent(Intent.ACTION_VIEW)
            licenseAgreementIntent.data = Uri.parse(getString(R.string.help_desk_link_to_license))
            startActivity(licenseAgreementIntent)
        }
    }

    private fun themeSwitcherCreate() {
        val themeSwitcher = findViewById<Switch>(R.id.themeSwitcher)
        if (App.darkTheme)
            themeSwitcher.isChecked = true
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }

}