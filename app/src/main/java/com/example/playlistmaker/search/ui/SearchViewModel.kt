package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.domain.api.TrackListInteractor
import com.example.playlistmaker.search.ui.models.SearchState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val handler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<SearchState>(SearchState.History(searchHistoryProvider.getSearchHistory()))
    private var latestSearchText: String? = null
    private var isClickAllowed = true
    private val trackListInteractor = Creator.provideTrackListInteractor()
    private lateinit var searchHistoryProvider: SearchHistoryProvider
    private var sharedPreference = application.getSharedPreferences(
        PLAYLIST_MAKER_SHARED_PREFERENCES, AppCompatActivity.MODE_PRIVATE
    )

    fun observeState(): LiveData<SearchState> = stateLiveData
init {
    searchHistoryProvider = SearchHistoryProvider(sharedPreference)
}


    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        const val SEARCH_HISTORY_KEY = "search_history"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_VALUE = "SEARCH_VALUE"

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
                SearchState.History(searchHistoryProvider.getSearchHistory())
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
        searchHistoryProvider.clearHistory()
        renderState(SearchState.History(searchHistoryProvider.getSearchHistory()))
    }

    fun clearSearch(){
        renderState(SearchState.Results(listOf<Track>()))
    }

    fun onTrackClicked(track: Track){
        if (clickDebounce()){
            searchHistoryProvider.addTrackToHistory(track)
            renderState(SearchState.History(searchHistoryProvider.getSearchHistory()))
        }
    }

    fun saveState(){
        sharedPreference.edit().putString(SEARCH_VALUE,latestSearchText)
    }

    fun getSearchFromState():String{
        return sharedPreference.getString(SEARCH_VALUE,"") ?: ""
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
