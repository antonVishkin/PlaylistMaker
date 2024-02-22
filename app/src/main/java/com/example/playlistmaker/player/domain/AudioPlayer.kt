package com.example.playlistmaker.player.domain

interface AudioPlayer {
    val currentPosition: Int
    var playerStatus: PlayerStatus
    fun start()
    fun pause()
    fun playbackControl(): PlayerStatus
    fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)

}
