package com.example.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding

class RootActivity: AppCompatActivity() {
    private lateinit var binding:ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_root,MainFragment()).commit()
        setContentView(binding.root)
    }
}