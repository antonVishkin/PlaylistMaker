package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.PlayerStatus
import com.example.playlistmaker.domain.api.AudioPlayer
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import java.util.concurrent.Executors

class MediaPlayerInteractorImpl(private val audioPlayer: AudioPlayer): MediaPlayerInteractor {
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

    override fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        executor.execute{
            audioPlayer.prepare(url = url,onPrepared = onPrepared, onCompletion = onCompletion)
        }
    }

    override fun release() {
        audioPlayer.release()
    }
}