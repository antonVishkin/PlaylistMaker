package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(shareAppLink: String) {
        Intent(Intent.ACTION_SEND).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            putExtra(
                Intent.EXTRA_TEXT,
                shareAppLink
            )
            type = "text/plain"
            context.startActivity(this)
        }
    }

    override fun openLink(termsLink: String) {
        Intent(Intent.ACTION_VIEW).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse(termsLink)
            context.startActivity(this)
        }
    }

    override fun openEmail(supportEmailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, supportEmailData.email)
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.title)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.message)
            context.startActivity(this)
        }
    }
}