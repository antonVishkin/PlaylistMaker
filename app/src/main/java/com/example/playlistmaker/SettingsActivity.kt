package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }
        val sharingButton = findViewById<FrameLayout>(R.id.sharing_button)
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
        val writeToHelpDeskButton = findViewById<FrameLayout>(R.id.write_help_desk)
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
        val licenseAgreementButton = findViewById<FrameLayout>(R.id.license_agreement)
        licenseAgreementButton.setOnClickListener {
            val licenseAgreementIntent = Intent(Intent.ACTION_VIEW)
            licenseAgreementIntent.data = Uri.parse(getString(R.string.help_desk_link_to_license))
            startActivity(licenseAgreementIntent)
        }
    }
}