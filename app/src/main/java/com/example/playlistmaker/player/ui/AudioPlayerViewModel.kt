package com.example.playlistmaker.player.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.TrackItemAdapter
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.data.mediaplayer.PlayerStatus
import com.example.playlistmaker.player.ui.models.PlayerState


class AudioPlayerViewModel(application: Application):AndroidViewModel(application) {
    private var mediaPlayer = Creator.provideMediaPlayerInteractor()
    private var track = TrackItemAdapter.track
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<PlayerState>(track?.let { PlayerState.Prepared(it) })
    private val timerLiveData = MutableLiveData(getApplication<Application>().getString(R.string.timer_zero))
    fun observePlayerState():LiveData<PlayerState> = stateLiveData
    fun observeTimer():LiveData<String> = timerLiveData

    init {
        preparePlayer()
    }

    companion object{
        private const val DELAY = 1000L
        fun getViewModelFactory():ViewModelProvider.Factory = viewModelFactory{
            initializer{
                AudioPlayerViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private fun preparePlayer() {
        track?.previewUrl?.let { mediaPlayer.prepare(it) }
    }


    private fun renderState(state: PlayerState){
        stateLiveData.postValue(state)
    }

    fun playClick() {
        when (mediaPlayer.playbackControl()) {
            PlayerStatus.STATE_PLAYING -> {
                mediaPlayer.start()
                startTimer()
                renderState(PlayerState.Playing)
            }
            else -> {
                mediaPlayer.pause()
                pauseTimer()
                renderState(PlayerState.Pause)
            }
        }
    }

    fun playerRelease(){
        pauseTimer()
        mediaPlayer.release()
    }


    private fun startTimer() {
        mainThreadHandler?.post(timerRunnable)
    }

    private fun pauseTimer() {
        mainThreadHandler?.removeCallbacks(timerRunnable)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.playerStatus == PlayerStatus.STATE_PLAYING) {
                val playedTime = mediaPlayer.currentPosition / DELAY
                timerLiveData.postValue(
                    String.format("%d:%02d", playedTime / 60, playedTime % 60))
                mainThreadHandler?.postDelayed(this, DELAY)
            }
        }
    }

}