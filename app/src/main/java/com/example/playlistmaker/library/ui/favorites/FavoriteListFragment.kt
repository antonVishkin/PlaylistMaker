package com.example.playlistmaker.library.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.EmptyFavoritesFragmentBinding
import com.example.playlistmaker.library.domain.favorites.FavoriteListState
import com.example.playlistmaker.player.domain.Track
import com.example.playlistmaker.search.ui.TrackItemAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteListFragment : Fragment() {

    private var binding: EmptyFavoritesFragmentBinding? = null
    private val viewModel: FavoriteListViewModel by viewModel()
    private lateinit var favoriteItemAdapter: TrackItemAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var onLongClicked:(Track) -> Unit = {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmptyFavoritesFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce =
            debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                val args = Bundle()
                args.putParcelable(TRACK, it)
                findNavController().navigate(
                    R.id.action_libraryFragment_to_audioPlayerFragment,
                    args
                )
            }
        favoriteItemAdapter = TrackItemAdapter (onTrackClickDebounce,onLongClicked)
        binding?.favoriteList?.adapter = favoriteItemAdapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.fillData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun renderState(state: FavoriteListState) {
        when (state) {
            is FavoriteListState.Content -> showContent(state.favoriteList)
            FavoriteListState.Empty -> showEmpty()
            FavoriteListState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding?.apply {
            emptyFavoriteImage.visibility = View.GONE
            emptyFavoriteText.visibility = View.GONE
            favoriteList.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showContent(trackList: List<Track>) {
        binding?.apply {
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
        binding?.apply {
            emptyFavoriteImage.visibility = View.VISIBLE
            emptyFavoriteText.visibility = View.VISIBLE
            favoriteList.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TRACK = "TRACK"
        fun newInstance() = FavoriteListFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }
}