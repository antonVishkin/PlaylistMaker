package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMainBinding
import com.example.playlistmaker.library.ui.LibraryFragment
import com.example.playlistmaker.search.ui.SearchFragment
import com.example.playlistmaker.settings.ui.SettingsFragment

class MainFragment : Fragment() {
    private lateinit var binding:FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchButton = binding.searchButton
        val libraryButton = binding.libraryButton
        val settingsButton = binding.settingsButton
        searchButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_root,SearchFragment()).addToBackStack("search").commit()
        }
        libraryButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_root,LibraryFragment()).addToBackStack("library").commit()
        }
        settingsButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_root,SettingsFragment()).addToBackStack("settings").commit()
        }
    }
}