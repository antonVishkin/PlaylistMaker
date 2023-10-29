package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.PlayerStatus

interface AudioPlayer {
    val currentPosition: Int
    var playerStatus: PlayerStatus
    fun start()
    fun pause()
    fun playbackControl(): PlayerStatus
    fun prepare(url:String, onPrepared:()->Unit,onCompletion:()->Unit)
    fun release()
}
