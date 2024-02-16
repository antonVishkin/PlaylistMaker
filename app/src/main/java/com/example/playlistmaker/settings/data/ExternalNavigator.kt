package com.example.playlistmaker.settings.data

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)

}
