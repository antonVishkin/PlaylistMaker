package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.PlayerStatus

interface MediaPlayerInteractor {
    val currentPosition: Int
    val playerStatus: PlayerStatus
    fun start()
    fun pause()
    fun playbackControl(): PlayerStatus
    fun prepare(url:String, onPrepared:()->Unit,onCompletion:()->Unit)
    fun release()
}