package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.ui.models.SearchState
import com.example.playlistmaker.util.debounce

class SearchViewModel(
    application: Application,
    private val trackListInteractor: TrackListInteractor,
    private val trackHistoryInteractor: TrackHistoryInteractor
) : AndroidViewModel(application) {
    private var stateLiveData: MutableLiveData<SearchState> =
        MutableLiveData<SearchState>(SearchState.History(trackHistoryInteractor.getHistory()))
    private var latestSearchText: String? = null
    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY,viewModelScope,true){
        search(it)
    }

    fun observeState(): LiveData<SearchState> = stateLiveData


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        trackSearchDebounce(changedText)
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            renderState(
                SearchState.History(trackHistoryInteractor.getHistory())
            )
        } else {
            renderState(
                SearchState.Loading
            )

            trackListInteractor.searchTracks(searchText,
                object : TrackListInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        val trackList = mutableListOf<Track>()
                        if (foundTracks != null)
                            trackList.addAll(foundTracks)
                        when {
                            errorMessage != null -> {
                                renderState(
                                    SearchState.Empty
                                )
                            }

                            trackList.isEmpty() -> {
                                renderState(
                                    SearchState.Empty
                                )
                            }

                            else -> {
                                renderState(
                                    SearchState.Results(trackList)
                                )
                            }
                        }
                    }
                })
        }
    }

    fun clearHistory() {
        trackHistoryInteractor.clearHistory()
        renderState(SearchState.History(trackHistoryInteractor.getHistory()))
    }

    fun clearSearch() {
        renderState(SearchState.History(trackHistoryInteractor.getHistory()))
    }

    fun addTrackToHistory(track: Track) {
        trackHistoryInteractor.addTrackToHistory(track)
    }


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}
