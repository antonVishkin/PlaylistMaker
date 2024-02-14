package com.example.playlistmaker.player.data.mediaplayer.impl

import com.example.playlistmaker.player.data.mediaplayer.PlayerStatus
import com.example.playlistmaker.player.data.mediaplayer.api.AudioPlayer
import com.example.playlistmaker.player.data.mediaplayer.api.MediaPlayerInteractor
import java.util.concurrent.Executors

class MediaPlayerInteractorImpl(private val audioPlayer: AudioPlayer) : MediaPlayerInteractor {
    private val executor = Executors.newCachedThreadPool()

    override val currentPosition
        get() = audioPlayer.currentPosition
    override val playerStatus
        get() = audioPlayer.playerStatus

    override fun start() {
        audioPlayer.start()
    }

    override fun pause() {
        audioPlayer.pause()
    }

    override fun playbackControl(): PlayerStatus {
        return audioPlayer.playbackControl()
    }

    override fun prepare(url: String) {
        executor.execute {
            audioPlayer.prepare(url = url)
        }
    }

    override fun release() {
        audioPlayer.release()
    }
}