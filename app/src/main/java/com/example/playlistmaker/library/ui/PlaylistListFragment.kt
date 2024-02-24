package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.EmptyPlaylistFragmentBinding

class PlaylistListFragment:Fragment() {
    companion object{
        fun newInstance() = PlaylistListFragment()
    }
    private lateinit var binding: EmptyPlaylistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmptyPlaylistFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}