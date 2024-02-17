package com.example.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp(shareAppLink:String)
    fun openTerms(termsLink:String)
    fun openSupport(emailData: EmailData)
}
