package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.EmptyPlaylistFragmentBinding
import com.example.playlistmaker.library.domain.PlaylistListState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistListFragment : Fragment() {
    private lateinit var binding: EmptyPlaylistFragmentBinding
    private val viewModel: PlaylistListViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmptyPlaylistFragmentBinding.inflate(inflater, container, false)
        viewModel.observeState().observe(viewLifecycleOwner){
            renderState(it)
        }
        return binding.root
    }

    private fun renderState( state:PlaylistListState){
        when(state){
            is PlaylistListState.Content -> showContent()
            PlaylistListState.Empty -> showEmpty()
            PlaylistListState.Loading -> showLoading()
        }
    }

    private fun showContent() {
        binding.apply {
            emptyPlaylistText.visibility = View.GONE
            emptyPlaylistsImage.visibility = View.GONE
            newPlaylistButton.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.apply {
            emptyPlaylistText.visibility = View.GONE
            emptyPlaylistsImage.visibility = View.GONE
            newPlaylistButton.visibility = View.GONE
        }
    }

    private fun showEmpty() {
        binding.apply {
            emptyPlaylistText.visibility = View.VISIBLE
            emptyPlaylistsImage.visibility = View.VISIBLE
            newPlaylistButton.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = PlaylistListFragment()
    }
}