package com.example.playlistmaker.search.ui

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.PLAYLIST_MAKER_SHARED_PREFERENCES
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.hideKeyboard
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.ui.models.SearchState
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
    private lateinit var viewModel: SearchViewModel
    private val trackProvider = arrayListOf<Track>()
    private lateinit var historyTrackListAdapter: TrackItemAdapter
    private val simpleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            visibilityClearButton(s)
            viewModel.searchDebounce(
                changedText = s?.toString() ?: ""
            )
        }

        override fun afterTextChanged(s: Editable?) {
            //empty
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        viewModel = ViewModelProvider(this,SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]
        searchProgressBarCreate()
        searchHistoryCreation()
        backButtonCreate()
        trackListCreation()
        searchEditTextCreate()
        viewModel.observeState().observe(this){
            render(it)
        }
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
                    viewModel.searchDebounce(searchEditText.text.toString())
                }
                true
            }
            false
        }
        refreshButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener {
            viewModel.searchDebounce(searchEditText.text.toString())
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
        }
    }

    enum class VisibleContent {
        SEARCH_RESULT, SEARCH_HISTORY, NO_SEARCH_RESULT, NO_INTERNET, SEARCH_PROGRESS_BAR
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

    private fun render(state: SearchState){
        when(state){
            SearchState.Empty -> showEmptyState()
            is SearchState.History -> showHistory()
            SearchState.Loading -> showLoadingState()
            SearchState.NoInternet -> showNoInternet()
            is SearchState.Results -> showResultState(state.trackList)
        }
    }

    private fun showEmptyState(){
        searchProgressBar.visibility = View.INVISIBLE
        noInternet.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.INVISIBLE
        searchHistory.visibility = View.INVISIBLE
        noSearchResult.visibility = View.VISIBLE
    }

    private fun showLoadingState(){
        searchProgressBar.visibility = View.VISIBLE
        noInternet.visibility = View.INVISIBLE
        noSearchResult.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.INVISIBLE
        searchHistory.visibility = View.INVISIBLE
    }

    private fun showNoInternet(){
        searchProgressBar.visibility = View.INVISIBLE
        noSearchResult.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.INVISIBLE
        searchHistory.visibility = View.INVISIBLE
        noInternet.visibility = View.VISIBLE
    }

    private fun showResultState(trackList: List<Track>){
        searchProgressBar.visibility = View.INVISIBLE
        noInternet.visibility = View.INVISIBLE
        noSearchResult.visibility = View.INVISIBLE
        searchHistory.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.VISIBLE

        trackProvider.clear()
        trackProvider.addAll(trackList)
        searchListItemAdapter.notifyDataSetChanged()
    }

    private fun showHistory(){
        searchProgressBar.visibility = View.INVISIBLE
        noInternet.visibility = View.INVISIBLE
        noSearchResult.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.INVISIBLE
        if (searchHistoryProvider.getSize() > 0) searchHistory.visibility = View.VISIBLE
        else searchHistory.visibility = View.INVISIBLE
    }

    companion object {
        private const val SEARCH_VALUE = "SEARCH_VALUE"
        private var searchText: String? = null
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        const val SEARCH_HISTORY_KEY = "search_history"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}