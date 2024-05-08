package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.hideKeyboard
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.ui.models.SearchState
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
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
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var onLongClicked:(Track) -> Unit = {}

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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce =
            debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                viewModel.addTrackToHistory(it)
                val args = Bundle()
                args.putParcelable(TRACK, it)
                findNavController().navigate(
                    R.id.action_searchFragment_to_audioPlayerFragment,
                    args
                )
            }
        searchProgressBarCreate()
        searchHistoryCreation()
        trackListCreation()
        searchEditTextCreate()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun searchProgressBarCreate() {
        searchProgressBar = binding.progressBar
        searchProgressBar.visibility = View.INVISIBLE
    }


    private fun searchEditTextCreate() {
        noSearchResult = binding.noSearchResult
        noInternet = binding.noInternet
        searchClearButton = binding.searchClear
        searchEditText = binding.searchForm
        searchClearButton.setOnClickListener {
            searchEditText.setText("")
            it.hideKeyboard()
            searchEditText.clearFocus()
            viewModel.clearSearch()
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(searchEditText.text.toString())
                true
            }
            false
        }
        refreshButton = binding.refreshButton
        refreshButton.setOnClickListener {
            viewModel.searchDebounce(searchEditText.text.toString())
        }
    }

    private fun visibilityClearButton(s: CharSequence?) {
        if (s.isNullOrEmpty()) searchClearButton.visibility = View.INVISIBLE
        else searchClearButton.visibility = View.VISIBLE
    }


    private fun trackListCreation() {
        searchListItemAdapter = TrackItemAdapter(onTrackClickDebounce,onLongClicked)
        trackItemsRecyclerView = binding.trackList
        trackItemsRecyclerView.adapter = searchListItemAdapter
    }

    private fun searchHistoryCreation() {
        searchHistory = binding.searchHistory
        historyTrackList = binding.historyTrackList
        clearHistoryButton = binding.clearHistory
        historyTrackListAdapter = TrackItemAdapter (onTrackClickDebounce,onLongClicked)
        historyTrackList.adapter = historyTrackListAdapter
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

    companion object {
        private const val TRACK = "TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}