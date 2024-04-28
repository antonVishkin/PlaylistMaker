package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.EmptyFavoritesFragmentBinding
import com.example.playlistmaker.library.domain.FavoriteListState
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.ui.TrackItemAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteListFragment : Fragment() {

    private lateinit var binding: EmptyFavoritesFragmentBinding
    private val viewModel: FavoriteListViewModel by viewModel()
    private lateinit var favoriteItemAdapter: TrackItemAdapter
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
        favoriteItemAdapter = TrackItemAdapter {
            //TODO(onTrackClicked)
        }
        binding.favoriteList.adapter = favoriteItemAdapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
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
    }
}