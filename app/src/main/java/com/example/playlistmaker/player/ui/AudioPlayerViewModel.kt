package com.example.playlistmaker.player.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerStatus
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.models.PlayerState


class AudioPlayerViewModel(
    application: Application,
    private val mediaPlayer: MediaPlayerInteractor
) :
    AndroidViewModel(application) {
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<PlayerState>()
    private lateinit var track: Track
    private val timerLiveData =
        MutableLiveData(getApplication<Application>().getString(R.string.timer_zero))

    fun observePlayerState(): LiveData<PlayerState> = stateLiveData
    fun observeTimer(): LiveData<String> = timerLiveData

    companion object {
        private const val DELAY = 1000L
    }

    fun preparePlayer(track: Track) {
        this.track = track
        track.previewUrl.let {
            mediaPlayer.prepare(
                it,
                onPrepared = onPrepared(),
                onCompletion = onCompletion()
            )
        }
    }

    fun onPause() {
        when (mediaPlayer.playerStatus) {
            PlayerStatus.STATE_PLAYING -> playClick()
            else -> {}
        }
    }


    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    fun playClick() {
        when (mediaPlayer.playbackControl()) {
            PlayerStatus.STATE_PLAYING -> {
                startTimer()
                renderState(PlayerState.Playing)
            }

            else -> {
                pauseTimer()
                renderState(PlayerState.Pause)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mainThreadHandler.removeCallbacks(timerRunnable)
    }


    private fun startTimer() {
        mainThreadHandler.post(timerRunnable)
    }

    private fun pauseTimer() {
        mainThreadHandler.removeCallbacks(timerRunnable)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.playerStatus == PlayerStatus.STATE_PLAYING) {
                val playedTime = mediaPlayer.currentPosition / DELAY
                timerLiveData.postValue(
                    String.format("%d:%02d", playedTime / 60, playedTime % 60)
                )
                mainThreadHandler.postDelayed(this, DELAY)
            }
        }
    }

    private fun onPrepared(): () -> Unit = { renderState(PlayerState.Prepared(track)) }
    private fun onCompletion(): () -> Unit = { renderState(PlayerState.Pause) }
}