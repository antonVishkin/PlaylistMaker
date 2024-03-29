package com.example.playlistmaker.sharing.domain

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(shareAppLink: String) {
        externalNavigator.shareLink(shareAppLink)
    }

    override fun openTerms(termsLink: String) {
        externalNavigator.openLink(termsLink)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }

}