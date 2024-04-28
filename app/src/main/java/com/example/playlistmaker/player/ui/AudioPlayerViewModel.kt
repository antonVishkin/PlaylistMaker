package com.example.playlistmaker.player.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.FavoritesInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerStatus
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerViewModel(
    private val track: Track,
    application: Application,
    private val mediaPlayer: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
) :
    AndroidViewModel(application) {
    private var timerJob: Job? = null

    private val stateLiveData = MutableLiveData<PlayerState>()
    private val timerLiveData =
        MutableLiveData(getApplication<Application>().getString(R.string.timer_zero))
    private val isFavoriteLiveData = MutableLiveData(track.isFavorite)

    fun observePlayerState(): LiveData<PlayerState> = stateLiveData
    fun observeTimer(): LiveData<String> = timerLiveData
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    init {
        viewModelScope.launch{
            track.isFavorite = favoritesInteractor.isFavorite(track.trackId)>0
            isFavoriteLiveData.postValue(track.isFavorite)
        }
    }

    companion object {
        private const val DELAY = 300L
    }

    fun preparePlayer() {
        track.previewUrl.let {
            mediaPlayer.prepare(
                it,
                onPrepared = onPrepared(),
                onCompletion = onCompletion()
            )
        }
    }

    fun stopPlaying() {
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

    fun onFavoriteClicked() {
        if (track.isFavorite)
            viewModelScope.launch {
                favoritesInteractor.deleteTrackFromFavorites(track)
            }
        else
            viewModelScope.launch {
                favoritesInteractor.addTrackToFavorites(track)
            }
        track.isFavorite = !track.isFavorite
        isFavoriteLiveData.postValue(track.isFavorite)
    }


    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.playerStatus == PlayerStatus.STATE_PLAYING) {
                timerLiveData.postValue(getCurrentPlayerPosition())
                delay(DELAY)
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
    }

    private fun onPrepared(): () -> Unit = { renderState(PlayerState.Prepared(track)) }
    private fun onCompletion(): () -> Unit = {
        renderState(PlayerState.Pause)
        timerJob?.cancel()
        timerLiveData.postValue(getApplication<Application>().getString(R.string.timer_zero))
    }
}