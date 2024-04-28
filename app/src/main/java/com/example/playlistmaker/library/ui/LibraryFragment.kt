package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {
    private lateinit var backButton: ImageView
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.libraryViewPager.adapter =
            LibraryViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator =
            TabLayoutMediator(binding.libraryTabLayout, binding.libraryViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favorite_traks)
                    1 -> tab.text = getString(R.string.playlists)
                }
            }
        tabMediator.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}