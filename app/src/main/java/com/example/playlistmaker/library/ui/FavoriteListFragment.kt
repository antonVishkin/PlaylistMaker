package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.EmptyFavoritesFragmentBinding
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
        return binding.root
    }

    companion object {
        fun newInstance() = FavoriteListFragment()
    }
}