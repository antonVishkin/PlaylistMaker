package com.example.playlistmaker.library.ui.playlistdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.playlist.PlayListsInteractor
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlistdetails.PlaylistDetailsState
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDetailsViewModel(
    private val playlist: Playlist,
    private val sharingInteractor: SharingInteractor,
    private val playListsInteractor: PlayListsInteractor,
) : ViewModel() {

    private val playlistDetailsStateLiveData = MutableLiveData<PlaylistDetailsState>()
    fun observeState(): LiveData<PlaylistDetailsState> = playlistDetailsStateLiveData

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

    fun sharePlaylist() {
        var sharingText = playlist.name + "\n"
        if (!playlist.description.isNullOrEmpty())
            sharingText = sharingText + playlist.description + "\n"
        sharingText = sharingText + makeTrackNumberText(playlist.list.size) + "\n"
        val trackListText = playlist.list.foldIndexed("") { index, acc, track ->
            acc + index + "." + track.artistName + " - " + track.trackName + " (" + SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis.toLong()) +
                    ")\n"
        }
        sharingInteractor.shareApp(sharingText + trackListText)
    }

    fun removeTrack(track: Track) {
        Log.v("PLAYLIST", "track removing")
        viewModelScope.launch {
            renderState(
                PlaylistDetailsState.Content(
                    playListsInteractor.removeTrackFromPlaylist(
                        track,
                        playlist
                    )
                )
            )
        }
    }


    private fun renderState(state: PlaylistDetailsState) {
        playlistDetailsStateLiveData.postValue(state)
    }
}