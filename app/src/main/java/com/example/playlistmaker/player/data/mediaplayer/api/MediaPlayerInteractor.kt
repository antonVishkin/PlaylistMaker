package com.example.playlistmaker.player.data.mediaplayer.api

import com.example.playlistmaker.player.data.mediaplayer.PlayerStatus

interface MediaPlayerInteractor {
    val currentPosition: Int
    val playerStatus: PlayerStatus
    fun start()
    fun pause()
    fun playbackControl(): PlayerStatus
    fun prepare(url: String)
    fun release()
}