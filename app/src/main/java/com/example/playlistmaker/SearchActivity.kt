package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private var lastSearch = ""
    private lateinit var backButton: ImageView
    private lateinit var searchClearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var trackItemsRecyclerView: RecyclerView
    private lateinit var noSearchResult: LinearLayout
    private lateinit var noInternet: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var searchHistory: LinearLayout
    private lateinit var historyTrackList: RecyclerView
    private lateinit var clearHistoryButton: Button
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var searchHistoryProvider: SearchHistoryProvider
    private lateinit var searchListItemAdapter: TrackItemAdapter
    private lateinit var searchProgressBar: ProgressBar
    private val retrofit =
        Retrofit.Builder().baseUrl(Companion.ITUNES_BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val trackProvider = arrayListOf<Track>()
    private lateinit var historyTrackListAdapter: TrackItemAdapter
    private val simpleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            visibilityClearButton(s)
            if (searchEditText.hasFocus() && s?.isEmpty() == true) setVisibleContent(VisibleContent.SEARCH_HISTORY)
            if (s?.isEmpty() != true && lastSearch != searchEditText.text.toString()) {
                setVisibleContent(VisibleContent.SEARCH_PROGRESS_BAR)
                lastSearch = searchEditText.text.toString()
                searchDebounce()
            }
        }

        override fun afterTextChanged(s: Editable?) {
            //empty
        }
    }

    private fun search(searchText: String) {
        iTunesService.search(searchText).enqueue(object : Callback<SearchResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchResponse>, response: Response<SearchResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results!!.isNotEmpty()) {
                            trackProvider.clear()
                            trackProvider.addAll(response.body()?.results!!)
                            setVisibleContent(VisibleContent.SEARCH_RESULT)
                            searchListItemAdapter.notifyDataSetChanged()
                        } else {
                            trackProvider.clear()
                            searchListItemAdapter.notifyDataSetChanged()
                            setVisibleContent(VisibleContent.NO_SEARCH_RESULT)
                        }
                    }
                    else -> {
                        setVisibleContent(VisibleContent.NO_INTERNET)
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                setVisibleContent(VisibleContent.NO_INTERNET)
            }
        })
    }

    private val searchRunnable = Runnable {
        setVisibleContent(VisibleContent.SEARCH_PROGRESS_BAR)
        search(searchEditText.text.toString())
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchProgressBarCreate()
        searchHistoryCreation()
        backButtonCreate()
        trackListCreation()
        searchEditTextCreate()
    }

    private fun searchProgressBarCreate() {
        searchProgressBar = findViewById(R.id.progressBar)
        searchProgressBar.visibility = View.INVISIBLE
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_VALUE)
        searchEditText.setText(searchText)
    }

    private fun backButtonCreate() {
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            this.finish()
        }
    }

    private fun searchEditTextCreate() {
        noSearchResult = findViewById(R.id.no_search_result)
        noInternet = findViewById(R.id.no_internet)
        searchClearButton = findViewById(R.id.search_clear)
        searchEditText = findViewById(R.id.search_form)
        searchClearButton.setOnClickListener {
            searchEditText.setText("")
            it.hideKeyboard()
            searchEditText.clearFocus()
            trackProvider.clear()
            searchListItemAdapter.notifyDataSetChanged()
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
        searchEditText.requestFocus()
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (lastSearch != searchEditText.text.toString()) {
                    setVisibleContent(VisibleContent.SEARCH_PROGRESS_BAR)
                    lastSearch = searchEditText.text.toString()
                    searchDebounce()
                }
                true
            }
            false
        }
        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchEditText.text.isEmpty()) setVisibleContent(VisibleContent.SEARCH_HISTORY)
        }
        setVisibleContent(VisibleContent.SEARCH_HISTORY)
        refreshButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener {
            setVisibleContent(VisibleContent.SEARCH_PROGRESS_BAR)
            searchDebounce()
        }
    }

    private fun visibilityClearButton(s: CharSequence?) {
        if (s.isNullOrEmpty()) searchClearButton.visibility = View.INVISIBLE
        else searchClearButton.visibility = View.VISIBLE
        searchText = s.toString()
    }

    private fun trackListCreation() {
        searchListItemAdapter = TrackItemAdapter(trackProvider, onTrackClick)
        trackItemsRecyclerView = findViewById(R.id.trackList)
        trackItemsRecyclerView.adapter = searchListItemAdapter
    }

    private fun searchHistoryCreation() {
        sharedPreference = getSharedPreferences(
            PLAYLIST_MAKER_SHARED_PREFERENCES, MODE_PRIVATE
        )
        searchHistoryProvider = SearchHistoryProvider(sharedPreference)
        searchHistory = findViewById(R.id.search_history)
        historyTrackList = findViewById(R.id.history_track_list)
        clearHistoryButton = findViewById(R.id.clear_history)
        historyTrackListAdapter =
            TrackItemAdapter(searchHistoryProvider.getSearchHistory(), onTrackClick)
        historyTrackList.adapter = historyTrackListAdapter
        clearHistoryButton = findViewById(R.id.clear_history)
        clearHistoryButton.setOnClickListener {
            searchHistoryProvider.clearHistory()
            historyTrackListAdapter.notifyDataSetChanged()
            setVisibleContent(VisibleContent.SEARCH_HISTORY)
        }
    }

    enum class VisibleContent {
        SEARCH_RESULT, SEARCH_HISTORY, NO_SEARCH_RESULT, NO_INTERNET, SEARCH_PROGRESS_BAR
    }

    private fun setVisibleContent(v: VisibleContent) {
        when (v) {
            VisibleContent.SEARCH_HISTORY -> {
                searchProgressBar.visibility = View.INVISIBLE
                noInternet.visibility = View.INVISIBLE
                noSearchResult.visibility = View.INVISIBLE
                trackItemsRecyclerView.visibility = View.INVISIBLE
                if (searchHistoryProvider.getSize() > 0) searchHistory.visibility = View.VISIBLE
                else searchHistory.visibility = View.INVISIBLE
            }
            VisibleContent.SEARCH_RESULT -> {
                searchProgressBar.visibility = View.INVISIBLE
                noInternet.visibility = View.INVISIBLE
                noSearchResult.visibility = View.INVISIBLE
                trackItemsRecyclerView.visibility = View.VISIBLE
                searchHistory.visibility = View.INVISIBLE
            }
            VisibleContent.NO_SEARCH_RESULT -> {
                searchProgressBar.visibility = View.INVISIBLE
                noInternet.visibility = View.INVISIBLE
                noSearchResult.visibility = View.VISIBLE
                trackItemsRecyclerView.visibility = View.INVISIBLE
                searchHistory.visibility = View.INVISIBLE
            }
            VisibleContent.NO_INTERNET -> {
                searchProgressBar.visibility = View.INVISIBLE
                noInternet.visibility = View.VISIBLE
                noSearchResult.visibility = View.INVISIBLE
                trackItemsRecyclerView.visibility = View.INVISIBLE
                searchHistory.visibility = View.INVISIBLE
            }
            VisibleContent.SEARCH_PROGRESS_BAR -> {
                searchProgressBar.visibility = View.VISIBLE
                noInternet.visibility = View.INVISIBLE
                noSearchResult.visibility = View.INVISIBLE
                trackItemsRecyclerView.visibility = View.INVISIBLE
                searchHistory.visibility = View.INVISIBLE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    val onTrackClick: (Track) -> Unit = { track ->
        if (clickDebounce()) {
            searchHistoryProvider.addTrackToHistory(track)
            historyTrackListAdapter.notifyDataSetChanged()
            val playerIntent = Intent(this, AudioPlayerActivity::class.java)
            this.startActivity(playerIntent)
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

    companion object {
        private const val SEARCH_VALUE = "SEARCH_VALUE"
        private var searchText: String? = null
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        const val SEARCH_HISTORY_KEY = "search_history"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}