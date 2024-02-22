package com.example.playlistmaker.player.domain

interface MediaPlayerInteractor {
    val currentPosition: Int
    val playerStatus: PlayerStatus
    fun start()
    fun pause()
    fun playbackControl(): PlayerStatus
    fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
}