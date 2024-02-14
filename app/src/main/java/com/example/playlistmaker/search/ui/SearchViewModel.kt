package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.ui.models.SearchState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofit =
        Retrofit.Builder().baseUrl(SearchActivity.ITUNES_BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val handler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<SearchState>(SearchState.History)
    private var latestSearchText: String? = null

    fun observeState(): LiveData<SearchState> = stateLiveData

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        const val SEARCH_HISTORY_KEY = "search_history"
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
                SearchState.History
            )
        }else{
            renderState(
                SearchState.Loading
            )


            iTunesService.search(searchText).enqueue(object : Callback<SearchResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results!!.isNotEmpty()) {
                                renderState(
                                    SearchState.Results(response.body()?.results!!)
                                )
                            } else {
                                renderState(
                                    SearchState.Empty
                                )
                            }
                        }

                        else -> {
                            renderState(
                                SearchState.NoInternet
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    renderState(
                        SearchState.NoInternet
                    )
                }
            })
        }
    }


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}
