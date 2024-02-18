package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.hideKeyboard
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.domain.Track.Companion.TRACK
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.ui.models.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
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
    private lateinit var searchListItemAdapter: TrackItemAdapter
    private lateinit var historyTrackListAdapter: TrackItemAdapter
    private lateinit var searchProgressBar: ProgressBar
    private val viewModel: SearchViewModel by viewModel()
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


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchProgressBarCreate()
        searchHistoryCreation()
        backButtonCreate()
        trackListCreation()
        searchEditTextCreate()
        viewModel.observeState().observe(this) {
            render(it)
        }
    }

    private fun searchProgressBarCreate() {
        searchProgressBar = findViewById(R.id.progressBar)
        searchProgressBar.visibility = View.INVISIBLE
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
            viewModel.clearSearch()
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
        searchEditText.requestFocus()
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(searchEditText.text.toString())
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
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun trackListCreation() {
        searchListItemAdapter = TrackItemAdapter {
            viewModel.onTrackClicked(it)
            val playerIntent = Intent(this, AudioPlayerActivity::class.java)
            playerIntent.putExtra(TRACK, it)
            this.startActivity(playerIntent)
        }
        trackItemsRecyclerView = findViewById(R.id.trackList)
        trackItemsRecyclerView.adapter = searchListItemAdapter
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun searchHistoryCreation() {
        searchHistory = findViewById(R.id.search_history)
        historyTrackList = findViewById(R.id.history_track_list)
        clearHistoryButton = findViewById(R.id.clear_history)
        historyTrackListAdapter =
            TrackItemAdapter {
                viewModel.onTrackClicked(it)
                val playerIntent = Intent(this, AudioPlayerActivity::class.java)
                playerIntent.putExtra(TRACK, it)
                this.startActivity(playerIntent)
            }
        historyTrackList.adapter = historyTrackListAdapter
        clearHistoryButton = findViewById(R.id.clear_history)
        clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
    }


    private fun render(state: SearchState) {
        when (state) {
            SearchState.Empty -> showEmptyState()
            is SearchState.History -> showHistory(state.trackList)
            SearchState.Loading -> showLoadingState()
            SearchState.NoInternet -> showNoInternet()
            is SearchState.Results -> showResultState(state.trackList)
        }
    }

    private fun showEmptyState() {
        searchProgressBar.visibility = View.INVISIBLE
        noInternet.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.INVISIBLE
        searchHistory.visibility = View.INVISIBLE
        noSearchResult.visibility = View.VISIBLE
    }

    private fun showLoadingState() {
        searchProgressBar.visibility = View.VISIBLE
        noInternet.visibility = View.INVISIBLE
        noSearchResult.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.INVISIBLE
        searchHistory.visibility = View.INVISIBLE
    }

    private fun showNoInternet() {
        searchProgressBar.visibility = View.INVISIBLE
        noSearchResult.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.INVISIBLE
        searchHistory.visibility = View.INVISIBLE
        noInternet.visibility = View.VISIBLE
    }

    private fun showResultState(trackList: List<Track>) {
        searchProgressBar.visibility = View.INVISIBLE
        noInternet.visibility = View.INVISIBLE
        noSearchResult.visibility = View.INVISIBLE
        searchHistory.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.VISIBLE

        searchListItemAdapter.trackItems.clear()
        searchListItemAdapter.trackItems.addAll(trackList)
        searchListItemAdapter.notifyDataSetChanged()
    }

    private fun showHistory(trackList: List<Track>) {
        searchProgressBar.visibility = View.INVISIBLE
        noInternet.visibility = View.INVISIBLE
        noSearchResult.visibility = View.INVISIBLE
        trackItemsRecyclerView.visibility = View.INVISIBLE
        if (trackList.isNotEmpty()) {
            historyTrackListAdapter.trackItems.clear()
            historyTrackListAdapter.trackItems.addAll(trackList)
            historyTrackListAdapter.notifyDataSetChanged()
            searchHistory.visibility = View.VISIBLE
        } else
            searchHistory.visibility = View.INVISIBLE
    }
}