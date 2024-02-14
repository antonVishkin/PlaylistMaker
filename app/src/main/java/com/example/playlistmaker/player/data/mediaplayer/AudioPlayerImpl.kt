package com.example.playlistmaker.player.data.mediaplayer

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.mediaplayer.PlayerStatus.*
import com.example.playlistmaker.player.data.mediaplayer.api.AudioPlayer

class AudioPlayerImpl : AudioPlayer {
    private var mediaPlayer = MediaPlayer()
    override val currentPosition
        get() = mediaPlayer.currentPosition
    override var playerStatus = STATE_DEFAULT

    override fun start() {
        mediaPlayer.start()
        playerStatus = STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerStatus = STATE_PAUSED
    }

    override fun playbackControl(): PlayerStatus {
        return when (playerStatus) {
            STATE_PLAYING -> {
                pause()
                STATE_PAUSED
            }
            STATE_PREPARED, STATE_PAUSED -> {
                start()
                STATE_PLAYING
            }
            else -> {
                STATE_DEFAULT
            }
        }
    }

    override fun prepare(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStatus = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerStatus = STATE_PREPARED
        }
    }

    override fun release() {
        mediaPlayer.release()
    }
}