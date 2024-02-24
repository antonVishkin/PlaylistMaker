package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.libraryViewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager,lifecycle)
        tabMediator = TabLayoutMediator(binding.libraryTabLayout,binding.libraryViewPager){ tab,position ->
            when (position){
                0 -> tab.text = getString(R.string.favorite_traks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
        backButtonCreate()
    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
    private fun backButtonCreate() {
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            this.finish()
        }
    }
}