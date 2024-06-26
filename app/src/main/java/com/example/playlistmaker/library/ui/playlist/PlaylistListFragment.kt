package com.example.playlistmaker.library.ui.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.EmptyPlaylistFragmentBinding
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistListState
import com.example.playlistmaker.search.ui.SearchFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistListFragment : Fragment() {
    private lateinit var binding: EmptyPlaylistFragmentBinding
    private val viewModel: PlaylistListViewModel by viewModel()
    private lateinit var playListItemAdapter: PlayListItemAdapter
    private val onItemClicked:(Playlist) -> Unit = {
        val args = Bundle()
        args.putParcelable(PLAYLIST, it)
        findNavController().navigate(R.id.action_libraryFragment_to_playlistDetailsFragment,args)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmptyPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playlistsList.layoutManager = GridLayoutManager(this.context, 2)
        playListItemAdapter = PlayListItemAdapter(onItemClicked)
        binding.playlistsList.adapter = playListItemAdapter
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_playListCreationFragment)
        }
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.fillData()

    }

    private fun renderState(state: PlaylistListState) {
        when (state) {
            is PlaylistListState.Content -> showContent(state.playlistList)
            PlaylistListState.Empty -> showEmpty()
            PlaylistListState.Loading -> showLoading()
        }
    }

    private fun showContent(list: List<Playlist>) {
        binding.apply {
            emptyPlaylistText.visibility = View.GONE
            emptyPlaylistsImage.visibility = View.GONE
            newPlaylistButton.visibility = View.VISIBLE
            playlistsList.visibility = View.VISIBLE
        }
        playListItemAdapter.playListsList.clear()
        playListItemAdapter.playListsList.addAll(list)
        playListItemAdapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        binding.apply {
            emptyPlaylistText.visibility = View.GONE
            emptyPlaylistsImage.visibility = View.GONE
            newPlaylistButton.visibility = View.GONE
            playlistsList.visibility = View.GONE
        }
    }

    private fun showEmpty() {
        binding.apply {
            emptyPlaylistText.visibility = View.VISIBLE
            emptyPlaylistsImage.visibility = View.VISIBLE
            newPlaylistButton.visibility = View.VISIBLE
            playlistsList.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance() = PlaylistListFragment()
        private const val PLAYLIST = "PLAYLIST"
    }
}