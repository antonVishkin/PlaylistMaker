package com.example.playlistmaker.library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListCreationFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistCreationBinding
    private val viewModel: PlayListCreationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return binding.root
    }
}