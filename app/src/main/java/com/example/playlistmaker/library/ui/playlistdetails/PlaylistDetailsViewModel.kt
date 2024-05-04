package com.example.playlistmaker.library.ui.playlistdetails

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.playlist.Playlist

class PlaylistDetailsViewModel(private val playlist: Playlist) : ViewModel() {


    fun fillData() {

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
}