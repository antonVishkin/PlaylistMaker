package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.playlistmaker.databinding.EmptyFavoritesFragmentBinding

class FavoriteListFragment: Fragment() {
    companion object{
        fun newInstance() = FavoriteListFragment()
    }
    private lateinit var binding: EmptyFavoritesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmptyFavoritesFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}