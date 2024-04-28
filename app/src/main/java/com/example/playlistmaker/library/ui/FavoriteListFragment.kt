package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.EmptyFavoritesFragmentBinding
import com.example.playlistmaker.library.domain.FavoriteListState
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.ui.TrackItemAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteListFragment : Fragment() {

    private lateinit var binding: EmptyFavoritesFragmentBinding
    private val viewModel: FavoriteListViewModel by viewModel()
    private lateinit var favoriteItemAdapter: TrackItemAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmptyFavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce =
            debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                findNavController().navigate(
                    R.id.action_libraryFragment_to_audioPlayerActivity,
                    AudioPlayerActivity.createArgs(it)
                )
            }
        favoriteItemAdapter = TrackItemAdapter {
            onTrackClickDebounce(it)
        }
        binding.favoriteList.adapter = favoriteItemAdapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.fillData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fillData()
    }

    private fun renderState(state: FavoriteListState) {
        when (state) {
            is FavoriteListState.Content -> showContent(state.favoriteList)
            FavoriteListState.Empty -> showEmpty()
            FavoriteListState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.apply {
            emptyFavoriteImage.visibility = View.GONE
            emptyFavoriteText.visibility = View.GONE
            favoriteList.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showContent(trackList: List<Track>) {
        binding.apply {
            emptyFavoriteImage.visibility = View.GONE
            emptyFavoriteText.visibility = View.GONE
            favoriteItemAdapter.trackItems.clear()
            favoriteItemAdapter.trackItems.addAll(trackList)
            favoriteItemAdapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
            favoriteList.visibility = View.VISIBLE

        }
    }

    private fun showEmpty() {
        binding.apply {
            emptyFavoriteImage.visibility = View.VISIBLE
            emptyFavoriteText.visibility = View.VISIBLE
            favoriteList.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance() = FavoriteListFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}