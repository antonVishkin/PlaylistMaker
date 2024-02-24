package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.EmptyPlaylistFragmentBinding
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
        return binding.root
    }

    companion object {
        fun newInstance() = PlaylistListFragment()
    }
}