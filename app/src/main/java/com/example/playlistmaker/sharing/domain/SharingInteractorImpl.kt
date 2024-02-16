package com.example.playlistmaker.sharing.domain

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.EmailData
import com.example.playlistmaker.sharing.data.ExternalNavigator

class SharingInteractorImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.share_application)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            arrayOf(context.getString(R.string.help_desk_post_email)),
            context.getString(R.string.help_desk_post_title),
            context.getString(R.string.help_desk_post_text)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.help_desk_link_to_license)
    }
}