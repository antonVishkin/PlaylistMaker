package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.PLAYLIST_MAKER_SHARED_PREFERENCES
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.search.data.SearchHistoryProvider
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.ui.models.SearchState

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val handler = Handler(Looper.getMainLooper())
    private var stateLiveData: MutableLiveData<SearchState>
    private var latestSearchText: String? = null
    private var isClickAllowed = true
    private val trackListInteractor = Creator.provideTrackListInteractor()
    private val trackHistoryInteractor = Creator.provideTrackHistoryInteractor(application)

    fun observeState(): LiveData<SearchState> = stateLiveData
init {
    stateLiveData = MutableLiveData<SearchState>(SearchState.History(trackHistoryInteractor.getHistory()))
}


    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
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
        }else{
            renderState(
                SearchState.Loading
            )

            trackListInteractor.searchTracks(searchText,object :TrackListInteractor.TracksConsumer {
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

    fun clearHistory(){
        trackHistoryInteractor.clearHistory()
        renderState(SearchState.History(trackHistoryInteractor.getHistory()))
    }

    fun clearSearch(){
        renderState(SearchState.History(trackHistoryInteractor.getHistory()))
    }

    fun onTrackClicked(track: Track){
        if (clickDebounce()){
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
