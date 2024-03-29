package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.ui.models.SearchState

class SearchViewModel(
    application: Application,
    private val trackListInteractor: TrackListInteractor,
    private val trackHistoryInteractor: TrackHistoryInteractor
) : AndroidViewModel(application) {
    private val handler = Handler(Looper.getMainLooper())
    private var stateLiveData: MutableLiveData<SearchState> =
        MutableLiveData<SearchState>(SearchState.History(trackHistoryInteractor.getHistory()))
    private var latestSearchText: String? = null
    private var isClickAllowed = true

    fun observeState(): LiveData<SearchState> = stateLiveData


    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { search(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
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

    fun onTrackClicked(track: Track) {
        if (clickDebounce()) {
            trackHistoryInteractor.addTrackToHistory(track)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}
