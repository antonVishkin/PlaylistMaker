package com.example.playlistmaker.player.data.mediaplayer.api

import com.example.playlistmaker.player.data.mediaplayer.PlayerStatus

interface AudioPlayer {
    val currentPosition: Int
    var playerStatus: PlayerStatus
    fun start()
    fun pause()
    fun playbackControl(): PlayerStatus
    fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun release()

}
