package com.example.playlistmaker.library.ui.playlistdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlistdetails.PlaylistDetailsState

class PlaylistDetailsViewModel(private val playlist: Playlist) : ViewModel() {

    private val playlistDetailsStateLiveData = MutableLiveData<PlaylistDetailsState>()
    fun observeState():LiveData<PlaylistDetailsState> = playlistDetailsStateLiveData
    init {
        renderState(PlaylistDetailsState.Content(playlist))
    }

    fun makeTrackNumberText(number: Int): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number трек"
            number % 10 in 2..4 && number % 100 !in 12..14 -> "$number трека"
            else -> "$number треков"
        }
    }

    fun countUniteTime(): String {
        val time =
            playlist.list.map { it.trackTimeMillis }.fold(0) { acc: Int, time: Int -> acc + time }
        val timeInMinutes = time / 60000
        return when {
            timeInMinutes % 10 == 1 && timeInMinutes % 100 != 11 -> "$timeInMinutes минута"
            timeInMinutes % 10 in 2..4 && timeInMinutes % 100 !in 12..14 -> "$timeInMinutes минуты"
            else -> "$timeInMinutes минут"
        }
    }

    private fun renderState(state: PlaylistDetailsState){
        playlistDetailsStateLiveData.postValue(state)
    }
}