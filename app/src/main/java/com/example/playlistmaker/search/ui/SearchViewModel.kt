package com.example.playlistmaker.search.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.ui.models.SearchState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val trackListInteractor: TrackListInteractor,
    private val trackHistoryInteractor: TrackHistoryInteractor
) : AndroidViewModel(application) {
    private var stateLiveData: MutableLiveData<SearchState> =
        MutableLiveData<SearchState>(SearchState.Loading)

    init {
        getHistory()
    }

    private var latestSearchText: String? = null
    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
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
            getHistory()
        } else {
            renderState(
                SearchState.Loading
            )
            viewModelScope.launch {
                trackListInteractor
                    .searchTracks(searchText)
                    .collect { (foundTracks, errorMessage) ->
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
            }
        }
    }

    private fun getHistory() {
        renderState(
            SearchState.Loading
        )
        viewModelScope.launch {
            trackHistoryInteractor.getHistory().collect {
                renderState(SearchState.History(it))
            }
        }
    }

    fun clearHistory() {
        trackHistoryInteractor.clearHistory()
        getHistory()
    }

    fun clearSearch() {
        getHistory()
    }

    fun addTrackToHistory(track: Track) {
        trackHistoryInteractor.addTrackToHistory(track)
    }


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}
