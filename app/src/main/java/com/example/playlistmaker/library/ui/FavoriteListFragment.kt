package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.EmptyFavoritesFragmentBinding
import com.example.playlistmaker.library.domain.FavoriteListState
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteListFragment : Fragment() {

    private lateinit var binding: EmptyFavoritesFragmentBinding
    private val viewModel: FavoriteListViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmptyFavoritesFragmentBinding.inflate(inflater, container, false)
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        return binding.root
    }

    private fun renderState(state: FavoriteListState) {
        when (state) {
            is FavoriteListState.Content -> showContent()
            FavoriteListState.Empty -> showEmpty()
            FavoriteListState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.apply {
            emptyFavoriteImage.visibility = View.GONE
            emptyFavoriteText.visibility = View.GONE
        }
    }

    private fun showContent() {
        binding.apply {
            emptyFavoriteImage.visibility = View.GONE
            emptyFavoriteText.visibility = View.GONE
        }
    }

    private fun showEmpty() {
        binding.apply {
            emptyFavoriteImage.visibility = View.VISIBLE
            emptyFavoriteText.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = FavoriteListFragment()
    }
}